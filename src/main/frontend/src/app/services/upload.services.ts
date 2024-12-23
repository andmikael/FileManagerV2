import { HttpClient } from '@angular/common/http';
import { Component, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
 providedIn: 'root'
})
export class UploadService {
 private uploadUrl = 'http://localhost:8080/api/index/uploadfile'; // Replace with your backend URL

 constructor(private httpClient: HttpClient) {}

 uploadFile(file: File): Observable<any> {
   const formData = new FormData();
   formData.append('file', file);
   return this.httpClient.post(this.uploadUrl, formData, {responseType: 'text', observe: 'response'});
 }
}