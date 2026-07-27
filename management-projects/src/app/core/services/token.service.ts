import { Injectable, Injector, runInInjectionContext, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TokenService {
  private readonly tokenKey = 'auth_token';
  private readonly refreshTokenKey = 'refresh_token';

  readonly token = signal<string | null>(this.getToken());

  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
    this.token.set(token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  removeToken(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.refreshTokenKey);
    this.token.set(null);
  }

  hasToken(): boolean {
    return !!this.getToken();
  }
}
