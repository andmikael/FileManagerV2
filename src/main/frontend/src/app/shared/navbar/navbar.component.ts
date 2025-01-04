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

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class NavbarComponent implements AfterViewInit{
  isLoggedIn: any

  constructor(
    private readonly userService: UserService,
    private readonly router: Router,
    private readonly http: HttpClient,
    private readonly errorHandlingService: ErrorHandlerService,
  ) {
    this.loadUser();
  }
  ngAfterViewInit(): void {
    this.isLoggedIn = localStorage.getItem('isLoggedIn')
  }

  logout() {
      this.http.post<any>(`${environment.apiUrl}/api/auth/logout`, null)
        .pipe(
          tap(() => {
            localStorage.removeItem('isLoggedIn')
            this.router.navigate(['/']);
            this.ngAfterViewInit();
          }),
          catchError((e) => this.errorHandlingService.handleError(e))
        ).subscribe()
  }

  login() {
  }

  loadUser()  {
  }
}
