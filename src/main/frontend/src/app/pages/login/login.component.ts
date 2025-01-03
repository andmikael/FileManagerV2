import { Component, OnInit, Output, EventEmitter, inject} from '@angular/core';
import { LoginFormComponent } from "./login-form/login-form.component";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { lastValueFrom, Observable, of, map, take, catchError, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AlertComponent } from '../../shared/alert/alert.component';
import { AlertService } from '../../services/alert.service';
import { AlertTypeEnum } from '../../shared/alert/alert.type.enum';
import { UserService } from '../../services/user.service';
import { ApiError } from '../../models/api.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LoginFormComponent, CommonModule, AlertComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(private alertService: AlertService,
    private userService: UserService) {}
  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);
  containers: any

  login(data: JSON) {
  let authorizationData = 'Basic ' + btoa(data + "");
  const headers = new HttpHeaders({
    'Authorization': authorizationData
  , 'UserRole' : 'user'})
    this.userService.login(headers)
    .pipe(take(1)
    ,tap(() => {
      this.router.navigate(["/containers"]);
    }), 
    catchError((e: ApiError) => {
        this.alertService.setAlert(e);
        return of();
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
    , 'UserRole' : 'trial'})
    this.userService.login(headers)
      .pipe(take(1)
      ,tap(() => {
        this.router.navigate(["/containers"]);
      }), 
      catchError((e: ApiError) => {
          this.alertService.setAlert(e);
          return of();
      })
    ).subscribe();
  }
}
