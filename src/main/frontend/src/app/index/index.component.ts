import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonEngine } from '@angular/ssr/node';
import { UploadService } from '../services/upload.services';
import { lastValueFrom, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './index.component.html',
  styleUrl: './index.component.css'
})
export class IndexComponent implements OnInit{
  constructor(private uploadService: UploadService) {
  }

  selectedFile: File | null = null; // Allowing 'null' as an initial value
  uploadStatus: string = 'waiting';

  blobs$: Observable<any> | undefined
  private apiUrl = environment.apiUrl;
  @ViewChild('fileInput') fileInput!: ElementRef;

  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);

  ngOnInit(): void {
    // used to initially populate blobs to a list but is also used for refreshing list after uploading
    // a blob. Current problem: GET request finishes too quickly and blob list is not updated with
    // the new blob
    this.blobs$ = this.http.get(`${this.apiUrl}`+"/api/index/")
  }

  handleFileSelection(event: any) {
    this.selectedFile = event.target.files[0];
    console.log('Selected file:', this.selectedFile);
  }
 
  async uploadFile() {
    if (this.selectedFile) {
      this.uploadStatus = 'inProgress';
 
      this.uploadService.uploadFile(this.selectedFile).subscribe({
        next: (response) => {
          console.log(response);
          this.uploadStatus = 'completed';
        },
        error: (error) => {
          console.log(error);
          this.uploadStatus = 'error';
        }})

    } else {
      alert("Please select a file before uploading.");
    }
    this.router.navigate(['/index'])
  }

  onUploadSubmit(uploadForm: NgForm) {
    this.uploadFile();
    this.fileInput.nativeElement.value = null;
    this.ngOnInit();
  }
}
