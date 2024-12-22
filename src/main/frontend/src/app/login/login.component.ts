import { Component, OnInit, Output, EventEmitter, inject} from '@angular/core';
import { LoginFormComponent } from "./login-form/login-form.component";
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [LoginFormComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  http: HttpClient = inject(HttpClient);
  login(data: JSON) {    
    this.http.post("http://localhost:8080/api/login", data)
    .subscribe({
      next: (response) => { 
        console.log(response); 
      },
      error: (error) => { 
        console.error(error); 
      }
    });
  }
}
