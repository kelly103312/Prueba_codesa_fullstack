import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { AuthService } from '../../services/auth.service';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  imports: [MenubarModule, ButtonModule, TooltipModule],
  templateUrl: './navbar.html',
})
export class Navbar {
  private authService = inject(AuthService);
  private router = inject(Router);

  items: MenuItem[] = [
    {
      label: 'Proyectos',
      icon: 'pi pi-folder',
      command: () => this.router.navigate(['/projects/home']),
    },
  ];

  get userName(): string {
    return this.authService.user()?.fullName ?? 'Usuario';
  }

  logout(): void {
    this.authService.logout();
  }
}
