import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';

export const PROJECTS_ROUTES: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: 'home',
    loadComponent: () => import('./home/home').then((m) => m.Home),
    canActivate: [authGuard],
  },
  {
    path: 'new',
    loadComponent: () => import('./project-form/project-form').then((m) => m.ProjectForm),
    canActivate: [authGuard],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./project-form/project-form').then((m) => m.ProjectForm),
    canActivate: [authGuard],
  },
];
