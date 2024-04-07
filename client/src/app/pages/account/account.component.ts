import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { CookiesService } from '../../services/cookies/cookies.service';

interface ExtendedJwtPayload extends JwtPayload {
  email: string;
  username: string;
  role: string;
  id: string;
}

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  user: ExtendedJwtPayload | null = this.cookie.get("token") ? jwtDecode(this.cookie.get("token") as string) : null;

  constructor(
    private http: HttpClient,
    private cookie: CookiesService
  ) {
    this.http.get(`http://localhost:8080/users/${this.user?.id}`)
      .subscribe((user: any) => {
        // this.data = user
      })
  }

  logout() {
    this.cookie.delete("token")
  }
}
