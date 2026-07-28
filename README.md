# Prueba FullStack - KELLY QUINTANA 

Sistema de gestión de proyectos con arquitectura de microservicios.

## Stack tecnológico

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.4.4 |
| Gateway | Spring Cloud Gateway 2024.0.1 |
| Base de datos | PostgreSQL 16 |
| ORM | Spring Data JPA + Flyway |
| Autenticación | JWT (jjwt 0.12.7) + BCrypt |
| Mapeo DTO | MapStruct 1.6.3 |
| Documentación | SpringDoc OpenAPI 2.8.6 |
| Frontend | Angular 20 |
| Contenedores | Docker + Docker Compose |

## Arquitectura

### Microservicios

| Servicio | Puerto | Base de datos | Descripción |
|----------|--------|---------------|-------------|
| **Gateway** | `:8080` | — | Spring Cloud Gateway, punto de entrada único |
| **User Service** | `:8001` | `user_db` | Autenticación, JWT, CRUD de usuarios |
| **Project Service** | `:8002` | `core_db` | CRUD de proyectos y tareas |

### Diagrama de flujo

```
Cliente (Angular :4200)
      |
      v
  Gateway (:8080)
      |
      +---> /api/auth/*    --> User Service (:8001) --> user_db
      +---> /api/users/*   --> User Service (:8001) --> user_db
      +---> /api/project/* --> Project Service (:8002) --> core_db
      +---> /api/task/*    --> Project Service (:8002) --> core_db
```

## Decisiones técnicas

### 1. Autenticación delegada (sin SSO)

Cada microservicio valida el JWT de forma independiente en lugar de centralizar en el gateway. Esto evita un punto único de fallo y permite que cada servicio sea autónomo. El gateway solo rutea, no autentica.

### 2. Contraseñas: SHA-256 en cliente + BCrypt en backend

El frontend aplica SHA-256 a la contraseña antes de enviarla, con la intención de que el backend nunca reciba ni procese la contraseña en texto plano ingresada por el usuario. El backend almacena `BCrypt(SHA-256(password))`, y en login compara con `BCrypt.matches()`.

**Limitación reconocida:** en términos estrictos de seguridad ante interceptación de red, el hash SHA-256 enviado por el cliente se convierte posible credencial, quien lo intercepte podría reenviarlo tal cual sin conocer la contraseña original (ataque tipo *pass-the-hash*), por lo que este esquema no sustituye la protección real que es HTTPS. Se mantiene como capa adicional consciente de esta limitación, priorizando que el servidor nunca procese el valor original ingresado por el usuario; en producción, HTTPS es obligatorio y es la protección efectiva contra interceptación, no el hash del lado del cliente.

### 3. Gateway como API Gateway
- Servicio Spring Cloud Gateway 
- `StripPrefix=1` elimina el prefijo `/api` antes de reenviar
- CORS configurado globalmente para `http://localhost:4200`
- Las rutas de documentación OpenAPI se agregan para Swagger unificado

### 4. MapStruct para mapeo DTO-Entity

Se eligió MapStruct sobre ModelMapper por:
- Generación de código en tiempo de compilación (zero overhead en runtime)
- Tipado seguro
- Mejor rendimiento

### 5. Flyway para migraciones de base de datos

Base de datos evolutiva con scripts SQL versionados. `ddl-auto: validate` en ambos servicios (user-service y project-service): Flyway es la única fuente que crea/modifica el esquema; Hibernate solo valida que las entidades `@Entity` coincidan con lo ya migrado, fallando de forma explícita ante cualquier desajuste en vez de alterar el esquema silenciosamente.

### 6. Roles y permisos (Project Service)

```
ADMIN → CRUD completo sobre cualquier proyecto y tarea, sin restricción de ownership
USER  → CRUD sobre proyectos y tareas donde es el assignedId (dueño real), según sección 8
```

### 7. Programación de tareas (Project Service)

El servicio tiene un scheduler (`@EnableScheduling`) que ejecuta cada hora un cron para marcar tareas como `OVERDUE` cuando su `dueDate` ha pasado, registrando en log (nivel INFO) la cantidad de tareas actualizadas. Vive en Project Service porque es el dueño de la tabla `tasks`, evitando depender de una llamada de red a otro servicio para leer/escribir su propio estado.

### 8. Ownership: creador vs. asignado

Cada proyecto distingue dos referencias a usuario, ambas como UUID sin FK real (viven en `user_db`, distinta a `core_db`):

- **`ownerId`**: el usuario que **creó** el proyecto. Se deriva siempre del JWT ya validado (`sub`), nunca del body del request, lo cual evita que se pueda suplantar la identidad de otro usuario al crear un recurso.
- **`assignedId`**: el usuario **dueño real** del proyecto a efectos de autorización. Por defecto es igual a `ownerId`

**Todas las reglas de acceso y el filtrado de listados usan `assignedId`**, nunca `ownerId`. Este último es solo información de trazabilidad (quién lo originó), no determina permisos. Esto se refleja en el endpoint `GET /project/assigned`, que retorna los proyectos donde el usuario autenticado es el `assignedId`.

`ownerName` / `assignedName` se denormalizan (se copian) al momento de la creación tomando el dato del JWT (`name`) para el creador, y del request para el usuario asignado de modo que Project Service pueda responder sin depender de una llamada en tiempo real a User Service. Se acepta que estos nombres puedan quedar desactualizados si el usuario cambia su `fullName` después de la creación del proyecto; es un trade-off estándar de denormalización de lectura en sistemas distribuidos.

## Manejo de JWT

### Generación (User Service)

- **Algoritmo:** SHA256 con clave secreta
- **Claims incluidos:**
  - `sub`: UUID del usuario
  - `name`: nombre completo
  - `role`: rol (ADMIN/USER)
  - `iat`, `exp`: fechas de emisión y expiración
- **Expiración:** configurable via `JWT_EXPIRATION` (default: 1 hora)
- **Secreto:** configurable via `JWT_SECRET`

### Validación en cada servicio

**User Service (`JwtAuthenticationFilter`):**
- Omite rutas `/auth/**` via `shouldNotFilter()`
- Si el header `Authorization` falta o no es `Bearer`, continúa la cadena sin autenticar
- Si el token es inválido/expirado → responde 401 JSON directamente
- Token válido → crea `UsernamePasswordAuthenticationToken` con rol

**Project Service (`JwtAuthenticationFilter`):**
- Omite requests `OPTIONS`
- Si hay header Bearer válido → extrae Claims y autentica
- Token inválido → continúa sin autenticar (el SecurityConfig maneja 401/403)

### Limitación conocida: JWT sin revocación

Al ser stateless, un cambio de rol o la desactivación de un usuario no se reflejan hasta que su token expire (máx. 1 hora). Se acepta como limitación conocida de este alcance; en producción se mitigaría con refresh tokens de vida corta.

### Recomendaciones para producción

1. **JWT_SECRET:** generar con `openssl rand -base64 32` (mínimo 32 bytes)
2. **CORS_ALLOWED_ORIGINS:** configurar con el dominio real del frontend
3. **HTTPS:** habilitar en el gateway (terminación SSL)
4. **Variables de entorno:** no usar valores default sensibles en `application.yml`
5. **Contraseñas:** usar un gestor de secretos (Vault, AWS Secrets Manager, etc.)
6. **Rate limiting:** agregar en el gateway para prevenir abusos en peticiones como `/auth/login`

## Documentación OpenAPI

Cada servicio expone su propia documentación. SpringDoc genera automáticamente la especificación OpenAPI 3.0 a partir de las anotaciones en controladores y DTOs.

| Servicio | Swagger UI | OpenAPI JSON |
|----------|------------|--------------|
| User Service | `http://localhost:8080/swagger-ui.html` | `http://localhost:8080/v3/api-docs` (Select a definition: User Service)|
| Project Service | `http://localhost:8080/swagger-ui.html` | `http://localhost:8080/v3/api-docs` (Select a definition: Project Service)|

En Swagger UI, usa el login User Service para obtener el token JWT y luego el botón **Authorize** de  para ingresar el token JWT y probar los endpoints protegidos.

## Cómo ejecutar

### Prerrequisitos

- Docker y Docker Compose

### Docker 

Para efectos practicos se escribe aqui las contraseñas sin embargo, es conocido que esto es una mala practica y se debe realizar esto por otros medios

```bash
# Variables de entorno requeridas en el .env
DB_PASSWORD=admin123
JWT_SECRET=una-clave-secreta-larga-de-al-menos-32-caracteres-para-hs256
JWT_EXPIRATION=3600000

# Levantar todo
docker-compose up --build
```

## Endpoints de la API

### User Service (`:8001`)

| Método | Path | Descripción | Auth |
|--------|------|-------------|------|
| POST | `/auth/login` | Iniciar sesión | No |
| POST | `/users/create` | Crear usuario | No |
| GET | `/users/{id}` | Obtener usuario por UUID | JWT |
| GET | `/users/me` | Usuario autenticado | JWT |
| GET | `/users/all` | Listar todos los usuarios | JWT |

### Project Service (`:8002`)

| Método | Path | Descripción | Auth |
|--------|------|-------------|------|
| POST | `/project/create` | Crear proyecto | JWT |
| PUT | `/project/update` | Actualizar proyecto | JWT |
| DELETE | `/project/{id}` | Eliminar proyecto | JWT |
| PATCH | `/project/status` | Cambiar estado del proyecto | JWT |
| GET | `/project/assigned` | Proyectos asignados al usuario (`assignedId`) | JWT |
| GET | `/project/{id}` | Obtener proyecto por ID | JWT |
| POST | `/task/{projectId}/create` | Crear tarea en un proyecto | JWT |
| PUT | `/task/update` | Actualizar tarea | JWT |
| GET | `/task/project/{projectId}` | Tareas de un proyecto | JWT |
| GET | `/task/{id}` | Obtener tarea por ID | JWT |
| DELETE | `/task/{id}` | Eliminar tarea | JWT |
| PATCH | `/task/status` | Cambiar estado de tarea | JWT |

### Gateway (`:8080`) — rutas con prefijo `/api`

| Path en gateway | Servicio destino | Path final |
|-----------------|------------------|------------|
| `/api/auth/**` | User Service | `/auth/**` |
| `/api/users/**` | User Service | `/users/**` |
| `/api/project/**` | Project Service | `/project/**` |
| `/api/task/**` | Project Service | `/task/**` |

## Estructura del proyecto

```
├── backend/
│   ├── demo/                  # User Service (autenticación, usuarios)
│   │   └── src/main/java/.../
│   │       ├── controller/    # AuthController, UserController
│   │       ├── dto/           # ApiResponse, AuthResponseDto, etc.
│   │       ├── entity/        # UserEntity
│   │       ├── exception/     # GlobalExceptionHandler + excepciones
│   │       ├── mapper/        # UserMapper (MapStruct)
│   │       ├── repository/    # UserRepository
│   │       ├── security/      # JwtService, JwtAuthenticationFilter, SecurityConfig
│   │       ├── service/       # AuthService, UserService
│   │   
│   ├── project-service/       # Project Service (proyectos, tareas)
│   │   └── src/main/java/.../
│   │       ├── controller/    # ProjectController, TaskController
│   │       ├── dto/           # DTOs de proyecto y tarea
│   │       ├── entity/        # ProjectEntity, TaskEntity
│   │       ├── enums/         # Role, ProjectStatus, TaskStatus
│   │       ├── exception/     # GlobalExceptionHandler + excepciones
│   │       ├── mapper/        # ProjectMapper, TaskMapper
│   │       ├── repository/    # ProjectRepository, TaskRepository
│   │       ├── scheduler/     # Scheduler para tareas vencidas
│   │       ├── security/      # JwtService, JwtAuthenticationFilter, SecurityConfig
│   │       └── service/       # ProjectService, TaskService
│   └── gateway/               # Spring Cloud Gateway
├── docs/                      # Documentación adicional
├── infra/                     # Scripts de infraestructura  para crear las dos BD'Ss
├── management-projects/       # Frontend Angular
├── docker-compose.yml         # Orquestación de contenedores
└── README.md                  # Este archivo
```
