import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login-form',
  imports: [FormsModule],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent {
  @Output() EmitLoginString: EventEmitter<JSON> = new EventEmitter<JSON>();

  onLoginSubmit(loginForm: NgForm) {
    this.EmitLoginString.emit(loginForm.value["connString"]);
  }
}
