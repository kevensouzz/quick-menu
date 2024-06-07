import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { CookiesService } from '../../cookies/cookies.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private endpoint = "http://localhost:8080/users"

  constructor(
    private http: HttpClient,
    private cookie: CookiesService
  ) { }

  register(email: String, username: String, cpf: string, password: String) {
    return this.http.post<{ token: string }>(`${this.endpoint}/register`, { email, username, cpf, password })
      .pipe(tap(response => {
        this.cookie.set("token", response.token, 2, "/")
        window.location.reload()
      }))
  }

  login(username: String, password: String) {
    return this.http.post<{ token: string }>(`${this.endpoint}/login`, { username, password })
      .pipe(tap(response => {
        this.cookie.set("token", response.token, 2, "/")
        window.location.reload()
      }))
  }
}
