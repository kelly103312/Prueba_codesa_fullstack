import { Injectable, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import { from, Observable, switchMap, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';
import { HashService } from './hash.service';
import { LoginRequest, LoginResponse, RegisterRequest, User } from '../models/auth';
import { ApiResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly userKey = 'auth_user';

  readonly user = signal<User | null>(this.loadUser());
  readonly isAuthenticated = signal(false);
  readonly isAdmin = computed(() => this.user()?.role === 'ADMIN');

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private hashService: HashService,
    private router: Router,
  ) {
    this.isAuthenticated.set(this.tokenService.hasToken());
  }
  hasRole(role: string): boolean {
    return this.user()?.role === role;
  }

  loadProfile(): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${environment.apiUrl}/users/me`).pipe(
      tap((res) => {
        this.user.set(res.data);
        this.persistUser(res.data);
      }),
    );
  }

  private persistUser(user: User): void {
    localStorage.setItem(this.userKey, JSON.stringify(user));
  }

  private loadUser(): User | null {
    try {
      const raw = localStorage.getItem(this.userKey);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  }

  private clearUser(): void {
    localStorage.removeItem(this.userKey);
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return from(this.hashService.sha256(credentials.password)).pipe(
      switchMap((hashedPassword) =>
        this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, {
          email: credentials.email,
          password: hashedPassword,
        }),
      ),
      tap((res: any) => {
        const data = res.data ?? res;
        this.tokenService.setToken(data.token);
        this.user.set(data.user);
        this.persistUser(data.user);
        this.isAuthenticated.set(true);
      }),
    );
  }

  logout(): void {
    this.tokenService.removeToken();
    this.clearUser();
    this.user.set(null);
    this.isAuthenticated.set(false);
    this.router.navigate(['/login']);
  }

  getProfile(): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${environment.apiUrl}/users/me`);
  }
}
