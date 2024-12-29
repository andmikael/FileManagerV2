import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, Injectable } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { take, tap } from 'rxjs';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class NavbarComponent {
  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);
  user$: any

  constructor(
    private readonly authService: AuthService,
    private readonly userService: UserService
  ) {
    this.user$ = this.userService.user$
    this.loadUser();
  }

  logout(): void {
    this.authService.logout().pipe(take(1)).subscribe();
    this.userService.clearUser();
    this.router.navigate(['/']);

  }

  login() {
    if (this.userService.user$ == null) {
      this.router.navigate(['/']);
    }
  }

  loadUser() {
    this.userService
    .getUser().pipe(
      take(1),
      tap(() => {
        localStorage.setItem('isLoggedIn', '1')
      }
    ),).subscribe();
  }
}
