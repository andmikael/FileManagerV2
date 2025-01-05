import { Component, OnInit, Output, EventEmitter, inject} from '@angular/core';
import { LoginFormComponent } from "./login-form/login-form.component";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { lastValueFrom, Observable, of, map, take, catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AlertComponent } from '../../shared/alert/alert.component';
import { AlertService } from '../../services/alert.service';
import { AlertTypeEnum } from '../../shared/alert/alert.type.enum';
import { UserService } from '../../services/user.service';
import { ApiError } from '../../models/api.model';
import { AuthService } from '../../services/auth.service';
import { ErrorHandlerService } from '../../services/error.handler.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LoginFormComponent, CommonModule, AlertComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(private alertService: AlertService,
    private authService: AuthService,
  private errorHandlingService: ErrorHandlerService) {
    }
  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);
  containers: any

  login(data: JSON) {
    let authorizationData = 'Basic ' + btoa(data + "");
    const headers = new HttpHeaders({'Authorization': authorizationData, 'UserRole' : 'ROLE_USER'})
    this.http.post<any>(`${environment.apiUrl}/api/auth/login`, null, { headers })
      .pipe(
        tap(() => {
          this.authService.setLoggedIn(true);
          this.navigateToContainer();
        }),
        catchError((e) => {
          this.errorHandlingService.handleError(e)
          this.alertService.setAlert(e)
          return throwError(e)
        })
      ).subscribe();
  }

  navigateToContainer() {
    this.router.navigate(["/containers"]);
  }

  loginWithTrial() {
    let authorizationData = 'Basic ' + btoa("trial" + "");
    const headers = new HttpHeaders({
      'Authorization': authorizationData
    , 'UserRole' : 'ROLE_TRIAL'})
    this.http.post<any>(`${environment.apiUrl}/api/auth/login`, null, { headers })
      .pipe(
        tap(() => {
          this.authService.setLoggedIn(true);
          this.navigateToContainer();
        }),
        catchError((e) => {
          this.errorHandlingService.handleError(e)
          this.alertService.setAlert(e)
          return throwError(e)
        })
      ).subscribe();
  }
}
