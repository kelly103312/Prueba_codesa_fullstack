import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import Aura from '@primeng/themes/aura';
import { definePreset } from '@primeng/themes';

import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';

const redTheme = definePreset(Aura, {
  semantic: {
    primary: {
      50: '#fbe7e7',
      100: '#f2b8b8',
      200: '#eb8a8a',
      300: '#e45c5c',
      400: '#de3d3d',
      500: '#990003',
      600: '#7a0002',
      700: '#5c0001',
      800: '#430001',
      900: '#2a0001',
      950: '#140000',
    },
    colorScheme: {
      light: {
        surface: {
          0: '#ffffff',
          50: '#f4f5f7',
          100: '#f4f5f7',
          200: '#e2e8f0',
          300: '#e2e8f0',
          400: '#cbd5e1',
          500: '#94a3b8',
          600: '#64748b',
          700: '#475569',
          800: '#334155',
          900: '#1e293b',
          950: '#0f172a',
        },
        primary: {
          color: '#990003',
          contrastColor: '#ffffff',
          hoverColor: '#7a0002',
          activeColor: '#5c0001',
        },
      },
    },
  },
});

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(
      withInterceptors([authInterceptor, errorInterceptor]),
    ),
    MessageService,
    providePrimeNG({
      theme: {
        preset: redTheme,
        options: {
          darkModeSelector: '.my-app-dark',
        },
      },
    }),
  ],
};