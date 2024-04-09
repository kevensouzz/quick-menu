import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { CookiesService } from '../../services/cookies/cookies.service';

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
  imports: [],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  token: string | null;
  userId: string | undefined;
  user: UserInterface | undefined

  constructor(
    private http: HttpClient,
    private cookie: CookiesService
  ) {
    this.token = this.cookie.get("token")

    if (this.token) {
      this.userId = jwtDecode<ExtendedJwtPayload>(this.token).id;
    }

    this.http.get<UserInterface>(`http://localhost:8080/users/${this.userId}`, {
      headers: {
        Authorization: `Bearer ${this.token}`
      }
    })
      .subscribe((data: UserInterface) => {
        this.user = data
      })
  }

  logout() {
    this.cookie.delete("token")
  }
}
