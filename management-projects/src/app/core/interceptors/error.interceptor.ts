import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { TokenService } from '../services/token.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const isLoginRequest = req.url.includes('/auth/login');

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401 && !isLoginRequest) {
        tokenService.removeToken();
        window.location.href = '/login';
      }

      const body = error.error;
      const message = body?.message || error.message || 'Error inesperado';

      console.error(`[API Error] ${error.status}: ${message}`);

      return throwError(() => ({
        status: error.status,
        message,
        code: body?.error || null,
        errors: body?.errors || null,
      }));
    }),
  );
};
