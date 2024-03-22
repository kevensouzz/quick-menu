import { Component } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [NgOptimizedImage, CommonModule, ReactiveFormsModule],
  providers: [AuthService],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent {
  hasAccount: boolean = true
  register!: FormGroup;
  login!: FormGroup;

  toggleHasAccount() {
    this.hasAccount = !this.hasAccount
  }

  constructor(private service: AuthService) {
    this.register = new FormGroup({
      email: new FormControl("", [Validators.required, Validators.email]),
      username: new FormControl("", [Validators.required, Validators.minLength(3), Validators.maxLength(16)]),
      password: new FormControl("", [Validators.required, Validators.minLength(8)]),
    })

    this.login = new FormGroup({
      username: new FormControl("", Validators.required),
      password: new FormControl("", Validators.required)
    })
  }

  registerSubmit() {
    if (this.register.valid) {
      this.service.register(this.register.value.email, this.register.value.username, this.register.value.password).subscribe({
        next: () => this.register.reset(),
        error: () => this.register.reset()
      })
    }
  }

  loginSubmit() {
    if (this.login.valid) {
      this.service.login(this.login.value.username, this.login.value.password).subscribe({
        next: () => this.login.reset(),
        error: () => this.login.reset()
      })
    }
  }
}
