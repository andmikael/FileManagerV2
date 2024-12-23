import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonEngine } from '@angular/ssr/node';
import { UploadService } from '../services/upload.services';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-index',
  imports: [RouterModule, CommonModule],
  templateUrl: './index.component.html',
  styleUrl: './index.component.css'
})
export class IndexComponent implements OnInit {
  constructor(private uploadService: UploadService) {}

  selectedFile: File | null = null; // Allowing 'null' as an initial value
  uploadStatus: string = 'waiting';

  blobs: String[] = []

  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);

  ngOnInit(): void {
      this.populateContainerList();
  }

    async getBlobs(): Promise<any> {
      try {
        return lastValueFrom(
          this.http.get("http://localhost:8080/api/index"));
      } catch (e) {
        console.log("error while trying to get blobs: " + e);
      }
    }
  
    async populateContainerList() {
      await this.getBlobs()
      .then(data => {
        this.blobs = data;
      }).catch(e => {
        console.log("error while getting blobs " + e)
      })
    }

  handleFileSelection(event: any) {
    this.selectedFile = event.target.files[0];
    console.log('Selected file:', this.selectedFile);
  }
 
  uploadFile() {
    if (this.selectedFile) {
      this.uploadStatus = 'inProgress';
 
      // Simulate upload with a timeout (replace this with actual upload logic)
      this.uploadService.uploadFile(this.selectedFile).subscribe({
        next: (response) => {
          console.log(response);
          this.uploadStatus = 'completed';
        },
        error: (error) => {
          console.log(error); 
          this.uploadStatus = 'error';
        }})
 
      // Add error handling and actual upload logic as needed
    } else {
      alert("Please select a file before uploading.");
    }
    this.ngOnInit();
  }
}
