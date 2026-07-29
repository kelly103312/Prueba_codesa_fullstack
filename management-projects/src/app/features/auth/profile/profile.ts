import { Component, inject, OnInit } from '@angular/core';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../core/models/auth';

@Component({
  selector: 'app-profile',
  imports: [CardModule, TagModule],
  templateUrl: './profile.html',
})
export class Profile implements OnInit {
  private authService = inject(AuthService);

  user: User | null = null;

  ngOnInit(): void {
    this.authService.loadProfile().subscribe({
      next: (res) => {
        this.user = res.data;
      },
      error: () => {
        this.user = this.authService.user();
      },
    });
  }

  get roleSeverity(): 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast' {
    switch (this.user?.role) {
      case 'ADMIN':
        return 'danger';
      case 'USER':
        return 'info';
      default:
        return 'secondary';
    }
  }
}
