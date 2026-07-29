import {  HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { map } from 'rxjs';

export const apiResponseInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    map((event) => {
      if (event instanceof HttpResponse && event.body) {
        const body = event.body as Record<string, unknown>;
        if (body['success'] === false || (typeof body['status'] === 'number' && body['status'] >= 400)) {
          const msg = String(body['message'] ?? 'Ha ocurrido un error inesperado');
          throw new Error(msg);
        }
      }
      return event;
    }),
  );
};
