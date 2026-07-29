package com.codesa.user.service.demo.controller;

import com.codesa.user.service.demo.dto.ApiResponse;
import com.codesa.user.service.demo.dto.AuthenticatedRequestDto;
import com.codesa.user.service.demo.dto.AuthResponseDto;
import com.codesa.user.service.demo.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para inicio de sesión")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con email y contraseña como hash SHA-256 (no texto plano), devuelve un JWT")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody AuthenticatedRequestDto request) {
        log.info("Llegó petición a /auth/login - email: {}, password: {}", request.getEmail(), request.getPassword());
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Inicio de sesión exitoso", response));
    }
}
