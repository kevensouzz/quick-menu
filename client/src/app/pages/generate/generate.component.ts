import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CookiesService } from '../../services/cookies/cookies.service';
import { ToastrService } from 'ngx-toastr';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CheckPassService } from '../../services/user/checkPassword/check-password.service';

interface ExtendedJwtPayload extends JwtPayload {
  id?: string;
  role?: string;
}

interface User {
  userId: string;
  email: string;
  username: string,
  picture: string,
  cpf: string,
  password: string,
  role: string,
  eateries: Eatery[];
}

interface Eatery {
  eateryId: string;
  name: string;
  phone: string;
}

@Component({
  selector: 'app-generate',
  standalone: true,
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './generate.component.html',
  styleUrl: './generate.component.css'
})
export class GenerateComponent {
  token: string | null | undefined;
  userId: string | undefined;
  user: User | undefined;
  confirmLogOut: boolean = false;
  confirmDeleteUser: boolean = false;
  checkPass: FormGroup;
  passwordMatch!: boolean;

  eateries: Eatery[] = [];
  selectedEatery: Eatery | null = null;
  newEatery: FormGroup;
  confirmDeleteEatery: boolean = false;

  constructor(
    private http: HttpClient,
    private checkPassword: CheckPassService,
    private cookie: CookiesService,
    private toast: ToastrService
  ) {
    this.loadUser()

    this.newEatery = new FormGroup({
      name: new FormControl("", [Validators.required]),
      description: new FormControl(""),
      phone: new FormControl("", [Validators.required, Validators.minLength(11), Validators.maxLength(11)]),
      address: new FormControl("", Validators.required),
      cnpj: new FormControl("", [Validators.required, Validators.minLength(14), Validators.maxLength(18)]),
    })
    
    this.checkPass = new FormGroup({
      password: new FormControl("", Validators.required)
    })
  }

  loadUser() {
    this.token = this.cookie.get("token")

    if (this.token) {
      this.userId = jwtDecode<ExtendedJwtPayload>(this.token).id;
    }

    return this.http.get(`http://localhost:8080/users/${this.userId}`, {
      headers: {
        Authorization: `Bearer ${this.token}`
      }
    }).subscribe((data: any) => {
      this.user = data;
      this.eateries = data.eateries
    })
  }

  logout() {
    this.cookie.delete("token")
    window.location.reload()
  }

  deleteUser(userId: string | undefined) {
    this.checkPassword.checkPass(this.checkPass.value.password, userId)
    .subscribe((data: any) => {
      this.passwordMatch = data

      if (this.passwordMatch) {
        this.http.delete(`http://localhost:8080/users/${userId}`, {
          headers: {
            Authorization: `Bearer ${this.token}`
          }
        })
          .subscribe({
            next: () => {
              this.cookie.delete("token")
              window.location.reload()
            }
          })
        this.passwordMatch = false
      } else {
        this.toast.error("Password Don't Match!")
      }
    })
  }

  formatPhone(event: any) {
    let input = event.target;
    let value = input.value.replace(/\D/g, '');
    if (value.length > 11) {
      value = value.slice(0, 11);
    }
    
    if (value.length > 6) {
      input.value = value.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    } else if (value.length > 2) {
      input.value = value.replace(/(\d{2})(\d{0,5})/, '($1) $2');
    } else {
      input.value = value;
    }
  }

  formatCNPJ(event: any) {
    let input = event.target;
    let value = input.value.replace(/\D/g, '');
    if (value.length > 14) {
      value = value.slice(0, 14);
    }

    if (value.length > 12) {
      input.value = value.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
    } else if (value.length > 8) {
      input.value = value.replace(/(\d{2})(\d{3})(\d{3})(\d{0,4})/, '$1.$2.$3/$4');
    } else if (value.length > 5) {
      input.value = value.replace(/(\d{2})(\d{3})(\d{0,3})/, '$1.$2.$3');
    } else if (value.length > 2) {
      input.value = value.replace(/(\d{2})(\d{0,3})/, '$1.$2');
    } else {
      input.value = value;
    }
  }
  
  newEaterySubmit(userId: string | undefined) {
    if (this.newEatery.valid) {
      const formValues = this.newEatery.value;
      const { name, description, phone, address, cnpj } = formValues;

      this.http.post(`http://localhost:8080/users/${userId}/eateries`,
        {
          name,
          description,
          phone,
          address,
          cnpj
        },
        {
          headers: {
            Authorization: `Bearer ${this.token}`,
          }
        })
        .subscribe({
          next: () => {
            this.newEatery.reset()
            this.toast.error("Successfully Created!")
          },
          error: () => {
            this.newEatery.reset()
            this.toast.error("Somenthing Wrong!")
          }
        })
    }
  }

  deleteEatery(userId: string | undefined, eateryId: string | undefined) {
    return this.http.delete(`http://localhost:8080/users/${userId}/eateries/${eateryId}`, {
      headers: {
        Authorization: `Bearer ${this.token}`
      }
    })
      .subscribe({
        error: () => {
          this.toast.error("Successfully Deleted!")
        }
      })
  }
}
