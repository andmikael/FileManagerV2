import { Component, OnInit, Output, EventEmitter, inject} from '@angular/core';
import { LoginFormComponent } from "./login-form/login-form.component";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { lastValueFrom, Observable, of, map, take } from 'rxjs';

@Component({
  selector: 'app-login',
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
    this.http.post("http://localhost:8080/api/login", data)
    .subscribe({
      next: (response) => {
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
}
