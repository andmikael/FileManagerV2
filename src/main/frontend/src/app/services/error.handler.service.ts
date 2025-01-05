/* eslint-disable no-console */
import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { ApiError } from '../models/api.model';

@Injectable({
  providedIn: 'root',
})
export class ErrorHandlerService {
  handleError(error: HttpErrorResponse): Observable<ApiError> {
    return throwError(
      (): ApiError => ({
        code: error.status,
        message: error.message || error.statusText,
        isError: true,
      }),
    );
  }
}