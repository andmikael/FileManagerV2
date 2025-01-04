import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../services/user.service';
import { BehaviorSubject, catchError, map, of, take, tap } from 'rxjs';
import { ApiUser } from '../../models/api.model';
import { environment } from '../../../environments/environment';
import { ErrorHandlerService } from '../../services/error.handler.service';
import { CookieOptions } from 'express';
import { authGuard } from '../../guards/auth.guard';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class NavbarComponent implements OnInit{
  isLoggedIn: any
  user$ : BehaviorSubject<ApiUser | null> = new BehaviorSubject<ApiUser | null>(null);

  constructor(
    private readonly userService: UserService,
    private readonly router: Router,
    private readonly http: HttpClient,
    private readonly errorHandlingService: ErrorHandlerService,
    private authService: AuthService,
  ) {
    this.loadUser();
  }
  ngOnInit(): void {
    this.router.events.pipe(
      tap((e) => {
        if(e.constructor.name === "NavigationEnd") {
          this.loadUser();
        }
      })
   ).subscribe()
  }

  logout() {
      this.http.post<any>(`${environment.apiUrl}/api/auth/logout`, null)
        .pipe(
          tap(() => {
            this.authService.setLoggedIn(false);
            if (this.router.url === "/") {
              window.location.reload();
            }
            this.router.navigate(['/']);
          }),
          catchError((e) => this.errorHandlingService.handleError(e))
        ).subscribe()
  }

  login() {
  }

  loadUser()  {
    this.http.get<ApiUser>(`${environment.apiUrl}/api/auth/user`)
    .subscribe({
      next: (user) => {
        if ((user.role === "ROLE_USER" || user.role === "ROLE_TRIAL")) {
          this.user$.next(user);
          if (!this.authService.getLoggedInStatus()) {
            this.authService.setLoggedIn(true);
          }
        } else {
          this.user$ = new BehaviorSubject<ApiUser | null>(null);
        }
      }, error: (e) => {
        console.log(e);
      }
    })
  }
}
