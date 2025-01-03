import { Injectable } from "@angular/core";
import { BehaviorSubject, catchError, take, tap } from "rxjs";
import { ApiError, ApiUser } from "../models/api.model";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Router } from "@angular/router";
import { ErrorHandlerService } from "./error.handler.service";

@Injectable({
    providedIn: 'root',
  })

export class UserService {

    readonly user$: BehaviorSubject<ApiUser | null> =
    new BehaviorSubject<ApiUser | null>(null);

    
    constructor(
        private http: HttpClient,
        private router: Router,
        private errorHandlingService: ErrorHandlerService
    ) {}

    setUser(user: ApiUser | null): void {
        this.user$.next(user)
      }

    getUser() {
        return this.http.get<ApiUser>(`${environment.apiUrl}/api/auth/user`).pipe(take(1),
           tap((user) => this.setUser(user)),
           catchError((e) => this.errorHandlingService.handleError(e)),
         );
    }

    clearUser() {
        this.setUser(null);
    }

    login(headers: HttpHeaders) {
        return this.http.post(`${environment.apiUrl}`+'/api/auth/login', null, {headers: headers})
        .pipe(catchError((e) => this.errorHandlingService.handleError(e)));
    }

    logout() {
        
    }
}