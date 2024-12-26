import { HttpClient } from '@angular/common/http';
import { Component, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
 providedIn: 'root'
})
export class UploadService {
  private apiUrl = environment.apiUrl;
  private uploadUrl = `${this.apiUrl}`+'api/index/uploadfile'; // Replace with your backend URL

 constructor(private httpClient: HttpClient) {}

 uploadFile(file: File): Observable<any> {
   const formData = new FormData();
   formData.append('file', file);
   return this.httpClient.post(this.uploadUrl, formData, {responseType: 'text', observe: 'response'});
 }
}