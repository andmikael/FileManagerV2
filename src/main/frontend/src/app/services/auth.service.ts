import { inject, Inject, Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";

@Injectable({
    providedIn: 'root'
  })

export class AuthService {
    constructor() {}

    http: HttpClient = inject(HttpClient);
    router: Router = inject(Router);

    logout() {
        localStorage.removeItem('isLoggedIn');
        return this.http.post<void>(`${environment.apiUrl}`+'/api/auth/logout', {})
    }

    login() {
        
    }
}