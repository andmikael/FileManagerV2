import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';


@Component({
  selector: 'app-root ',
  imports: [RouterOutlet, LoginComponent],
  template: '<app-login ngSkipHydration="true">',
  styleUrl: './login/login.component.css'
})
export class AppComponent {
  title = 'filemanager';
}
