import { HttpClient, HttpHeaders } from "@angular/common/http";
import { ErrorHandlerService } from "./error.handler.service";
import { catchError } from "rxjs";
import { environment } from "../../environments/environment";
import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root',
  })

export class AuthService {
    constructor(
        private http: HttpClient,
        private errorHandlingService: ErrorHandlerService,
    ) {}

    login(headers: HttpHeaders) {
        return this.http.post(`${environment.apiUrl}`+'/api/auth/login', {headers: headers})
        .pipe(catchError((e) => this.errorHandlingService.handleError(e)));
    }
}