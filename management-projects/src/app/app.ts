import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { Navbar } from './core/components/navbar/navbar';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ConfirmDialogModule, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  showNavbar = true;

  constructor(private router: Router) {
    this.router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe(() => {
      this.showNavbar = !this.router.url.startsWith('/login');
    });
  }
}
