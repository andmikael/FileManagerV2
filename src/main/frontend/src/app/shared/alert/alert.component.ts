import { Component, OnInit, signal } from '@angular/core';
import { AlertInterface, ApiError } from '../../models/api.model';
import { CommonModule } from '@angular/common';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.css'
})
export class AlertComponent implements OnInit {
  errorSig = signal<null | ApiError>(null);

  constructor(private alertService: AlertService) {}

  ngOnInit(): void {
    this.setAlert();
  }

  setAlert(): void {
    this.errorSig = this.alertService.getAlert();
  }

}
