import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then((m) => m.Login),
  },
  {
      path: 'projects',
      loadChildren: () => import('./features/projects/projects.routes')
         .then(m => m.PROJECTS_ROUTES)
  },
  {
      path: 'tasks',
      loadChildren: () => import('./features/tasks/tasks.routes')
         .then(m => m.TASKS_ROUTES)
  },
];
