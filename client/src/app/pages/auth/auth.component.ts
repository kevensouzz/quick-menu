import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/user/auth/auth.service';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
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

    this.register.setValue({ email: '', username: '', cpf: '',password: '' })
    this.login.setValue({ username: '', password: '' })
  }

  constructor(
    private service: AuthService,
    private toast: ToastrService,
    private http: HttpClient
  ) {
    this.http.get("http://localhost:8080/users")
      .subscribe()

    this.register = new FormGroup({
      email: new FormControl("", [Validators.required, Validators.email]),
      username: new FormControl("", [Validators.required, Validators.minLength(3), Validators.maxLength(16)]),
      cpf: new FormControl("", [Validators.required, Validators.minLength(11), Validators.maxLength(14)]),
      password: new FormControl("", [Validators.required, Validators.minLength(8)]),
    })

    this.login = new FormGroup({
      username: new FormControl("", Validators.required),
      password: new FormControl("", Validators.required)
    })
  }

  formatCPF(event: any) {
    let input = event.target;
    let value = input.value.replace(/\D/g, '');
    
    if (value.length > 11) {
      value = value.slice(0, 11);
    }

    if (value.length > 9) {
      input.value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    } else if (value.length > 6) {
      input.value = value.replace(/(\d{3})(\d{3})(\d{0,3})/, '$1.$2.$3');
    } else if (value.length > 3) {
      input.value = value.replace(/(\d{3})(\d{0,3})/, '$1.$2');
    } else {
      input.value = value;
    }
  }
  
  registerSubmit() {
    if (this.register.valid) {
      this.service.register(this.register.value.email, this.register.value.username, this.register.value.cpf, this.register.value.password)
        .subscribe({
          error: () => {
            this.register.reset()
            this.toast.error("Something Wrong!")
          }
        })
    }
  }

  loginSubmit() {
    if (this.login.valid) {
      this.service.login(this.login.value.username, this.login.value.password)
        .subscribe({
          error: () => {
            this.login.reset()
            this.toast.error("Something Wrong!")
          },
        }
        )
    }
  }
}
