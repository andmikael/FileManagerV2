import { Component, OnInit, Output, EventEmitter, inject} from '@angular/core';
import { LoginFormComponent } from "./login-form/login-form.component";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { lastValueFrom, Observable, of, map, take } from 'rxjs';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LoginFormComponent, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  showError: boolean = false;
  errorMessage: string = '';
  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);
  containers: any

  async login(data: JSON) {    
    this.http.post(`${environment.apiUrl}`+'/api/auth/login', data, {headers: {'Access-Control-Allow-Origin': '*'}})
    .subscribe({
      next: (response) => {
        localStorage.setItem('isLoggedIn', '1');
        this.navigateToContainer();
      },
      error: (error) => { 
        console.error(error);
        this.showError = true;
        this.errorMessage = 'Invalid connection string'; 
      }
    });
  }

  navigateToContainer() {
    this.router.navigate(["/containers"]);
  }

  selectTrialAccount() {
    this.http.post(`${environment.apiUrl}`+'/api/auth/login', "trial", {headers: {'Access-Control-Allow-Origin': '*'}})
    .subscribe({
      next: (response) => {
        localStorage.setItem('isLoggedIn', '1');
        this.navigateToContainer();
      },
      error: (error) => { 
        console.error(error);
        this.showError = true;
        this.errorMessage = 'was unable to login to trial account'; 
      }
    });
  }
}