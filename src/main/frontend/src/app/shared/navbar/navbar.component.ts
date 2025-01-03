import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../services/user.service';
import { map, take, tap } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class NavbarComponent {
  user$ = this.userService.user$

  readonly isLoggedIn$ = this.user$.pipe(map((user) => !!user));

  constructor(
    private readonly userService: UserService,
    private readonly router: Router
  ) {
    this.loadUser();
  }

  logout(): void {
    /*this.authService.logout().pipe(take(1)).subscribe();
    this.userService.clearUser();
    this.router.navigate(['/']);*/
  }

  login() {
    if (this.userService.user$ == null) {
      this.router.navigate(['/']);
    }
  }

  loadUser()  {
    this.userService
    .getUser()
      .pipe(
        take(1),
        tap(() => {
            localStorage.setItem('isLoggedIn', '1');
          }),
      )
      .subscribe();
  }
}
