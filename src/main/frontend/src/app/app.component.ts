import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { AlertComponent } from './shared/alert/alert.component';
import { AlertTypeEnum } from './shared/alert/alert.type.enum';
import { AlertService } from './services/alert.service';
import { LandingComponent } from './pages/landing/landing.component';


@Component({
  selector: 'app-root ',
  imports: [
    RouterOutlet,
    RouterModule,
    NavbarComponent,
  LandingComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
  title = 'filemanager';
  constructor() {}
}
