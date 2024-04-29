import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { CookiesService } from '../../services/cookies/cookies.service';
import { CommonModule } from '@angular/common';
import { UpdateService } from '../../services/user/update/update.service';
import { ToastrService } from 'ngx-toastr';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CheckPassService } from '../../services/user/checkPass/check-pass.service';
import { AuthService } from '../../services/user/auth/auth.service';

interface ExtendedJwtPayload extends JwtPayload {
  id?: string;
}

interface UserInterface {
  id: string
  email: string
  username: string
  password: string
  role: string
}

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  token: string | null;
  userId: string | undefined;
  user: UserInterface | undefined;

  passwordMatch!: boolean;
  usernameLogin!: String;
  passwordLogin!: String;

  UserInfo: boolean = true;
  updateUser: boolean = false;
  updateUserPass: boolean = false;
  confirmLogOut: boolean = false;
  confirmDelete: boolean = false;

  update: FormGroup;
  updatePass: FormGroup;
  deleteUser: FormGroup;

  constructor(
    private service: UpdateService,
    private checkPass: CheckPassService,
    private http: HttpClient,
    private toast: ToastrService,
    private cookie: CookiesService,
  ) {
    this.token = this.cookie.get("token")

    if (this.token) {
      this.userId = jwtDecode<ExtendedJwtPayload>(this.token).id;
    }

    this.http.get<UserInterface>(`http://localhost:8080/users/id/${this.userId}`, {
      headers: {
        Authorization: `Bearer ${this.token}`
      }
    })
      .subscribe((data: UserInterface) => {
        this.user = data
      })

    this.update = new FormGroup({
      email: new FormControl("", [Validators.required, Validators.email]),
      username: new FormControl("", [Validators.required, Validators.minLength(3), Validators.maxLength(16)]),
      passwordConfirm: new FormControl("", Validators.required),
    })

    this.updatePass = new FormGroup({
      oldPassword: new FormControl("", Validators.required),
      password: new FormControl("", [Validators.required, Validators.minLength(8)])
    })

    this.deleteUser = new FormGroup({
      password: new FormControl("", Validators.required)
    })
  }

  updateSubmit(id: string) {
    this.usernameLogin = this.update.value.username;
    this.passwordLogin = this.update.value.passwordConfirm;

    if (this.update.value.email == this.user?.email) {
      this.update.value.email = null
    }

    if (this.update.value.username == this.user?.username) {
      this.update.value.username = null
    }

    if (this.update.value.email == null && this.update.value.username == null) {
      return this.toast.error("There Isn't Changes!")
    }

    if (this.update.valid) {

      this.checkPass.checkPass(this.update.value.passwordConfirm, id)
        .subscribe((data: any) => {
          this.passwordMatch = data

          if (this.passwordMatch) {
            this.service.update(this.update.value.email, this.update.value.username, id)
              .subscribe()

            if (this.update.value.email != null) {

              this.http.post<{ token: string }>("http://localhost:8080/users/login", { username: this.usernameLogin, password: this.passwordLogin })
                .subscribe((response: { token: string }) => {
                  this.cookie.delete("token")
                  this.cookie.set("token", response.token, 2, "/")
                  window.location.reload()
                })

            } else if (this.update.value.username != null) {
              window.location.reload()
            }

            this.passwordMatch = false;
          } else {
            this.toast.error("Password Don't Match!")
          }
        })

    } else {
      return this.toast.error("Invalid Data!")
    }
    return
  }

  updatePassSubmit(id: string) {
    if (this.updatePass.valid) {
      this.checkPass.checkPass(this.updatePass.value.oldPassword, id)
        .subscribe((data: any) => {
          this.passwordMatch = data

          if (this.passwordMatch) {
            this.service.updatePass(this.updatePass.value.password, id)
              .subscribe({
                error: () => {
                  this.toast.error("Successfully Updated!")
                  this.updatePass.reset()
                }
              })
            this.passwordMatch = false
          } else {
            this.toast.error("Password Don't Match!")
          }
        }
        )
    } else {
      this.toast.error("Invalid Data!")
    }
  }

  logout() {
    this.cookie.delete("token")
    window.location.reload()
  }

  delete(id: string) {
    this.checkPass.checkPass(this.deleteUser.value.password, id)
      .subscribe((data: any) => {
        this.passwordMatch = data

        if (this.passwordMatch) {
          this.http.delete(`http://localhost:8080/users/delete/${id}`, {
            headers: {
              Authorization: `Bearer ${this.token}`
            }
          })
            .subscribe({
              error: () => {
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
}
