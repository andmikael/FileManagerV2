import { CommonModule } from '@angular/common';
import { Component, inject, Injectable } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { take } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})

export class NavbarComponent {
  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);

  constructor(
    private readonly authService: AuthService,
  ) {
    
  }

  logout(): void {
    this.authService.logout().pipe(take(1)).subscribe();
    this.router.navigate(['/']);
  }
}
