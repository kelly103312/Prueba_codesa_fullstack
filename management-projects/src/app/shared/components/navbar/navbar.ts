import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { MenuItem } from 'primeng/api';
import { AuthService } from '../../../core/services/auth.service';

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
  goToProfile(){
    this.router.navigate(['/login/profile'])
  }
}
