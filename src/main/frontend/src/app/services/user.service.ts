import { Injectable } from "@angular/core";
import { BehaviorSubject, tap } from "rxjs";
import { ApiUser } from "../models/api.model";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: 'root',
  })

export class UserService {

    readonly user$: BehaviorSubject<ApiUser | null> =
    new BehaviorSubject<ApiUser | null>(null);

    
    constructor(
        private http: HttpClient,
    ) {}

    setUser(user: ApiUser | null): void {
        this.user$.next(user);
      }

    getUser() {
        return this.http.get<ApiUser>(`${environment.apiUrl}/api/auth/`).pipe(
            tap((user) => this.setUser(user)))
    }

    clearUser() {
        this.setUser(null);
    }
}