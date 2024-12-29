import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonEngine } from '@angular/ssr/node';
import { UploadService } from '../../services/upload.services';
import { lastValueFrom, map, Observable, of, take, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { FormsModule, NgForm } from '@angular/forms';
import { BlobService } from '../../services/blob.service';

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './index.component.html',
  styleUrl: './index.component.css'
})
export class IndexComponent {
  blobs$: any;

  constructor(private uploadService: UploadService,
    private readonly blobService: BlobService
  ) {
    this.blobs$ = this.blobService.blobs$;
    this.loadBlobs();
  }

  @ViewChild('fileInput') fileInput!: ElementRef;

  selectedFile: File | null = null;
  uploadStatus: string = 'waiting';

  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);

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
  }

    loadBlobs() {
      this.blobService
      .getBlobs().pipe(
        take(1),
        tap((blob) => {}),).subscribe();
    }
}
