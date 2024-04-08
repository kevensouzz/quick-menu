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
  token = this.cookie.get("token")
  userId: string | undefined;
  user: UserInterface | undefined
  
  constructor(
    private http: HttpClient,
    private cookie: CookiesService
  ) {
    if (this.token) {
      const decodedToken = jwtDecode<ExtendedJwtPayload>(this.token);
      this.userId = decodedToken.id as string;
    }

    this.http.get<UserInterface>(`http://localhost:8080/users/${this.userId}`)
      .subscribe((data: UserInterface) => {
        this.user = data
      })
  }

  logout() {
    this.cookie.delete("token")
  }
}
