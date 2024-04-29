import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CookiesService } from '../../services/cookies/cookies.service';
import { JwtPayload, jwtDecode } from 'jwt-decode';

interface ExtendedJwtPayload extends JwtPayload {
  id?: string;
}

@Component({
  selector: 'app-generate',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './generate.component.html',
  styleUrl: './generate.component.css'
})
export class GenerateComponent {
  token: string | null;
  userId: string | undefined;

  constructor(
    private http: HttpClient,
    private cookie: CookiesService
  ) {
    this.token = this.cookie.get("token")

    if (this.token) {
      this.userId = jwtDecode<ExtendedJwtPayload>(this.token).id;
    }

    this.http.get(`http://localhost:8080/users/id/$${this.userId}`, {
      headers: {
        Authorization: `Bearer ${this.token}`
      }
    })
      .subscribe()
  }
}
