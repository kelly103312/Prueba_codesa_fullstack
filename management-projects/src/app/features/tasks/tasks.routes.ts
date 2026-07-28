import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';

export const TASKS_ROUTES: Routes = [
  {
    path: '',
    redirectTo: '/projects/home',
    pathMatch: 'full',
  },
  {
    path: 'new/:projectId',
    loadComponent: () => import('./task-form/task-form').then((m) => m.TaskForm),
    canActivate: [authGuard],
  },
  {
    path: ':taskId/edit/:projectId',
    loadComponent: () => import('./task-form/task-form').then((m) => m.TaskForm),
    canActivate: [authGuard],
  },
];
