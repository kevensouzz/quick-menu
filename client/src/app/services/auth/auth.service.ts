import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';

interface authResponse {
  token: string
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private endpoint = "http://localhost:8080/users"

  constructor(private http: HttpClient) { }

  register(email: String, username: String, password: String) {
    return this.http.post<authResponse>(`${this.endpoint}/register`, { email, username, password })
      .pipe(tap((response) => {
        document.cookie = `token=${response.token}`;
      }))
  }

  login(username: String, password: String) {
    return this.http.post<authResponse>(`${this.endpoint}/login`, { username, password })
      .pipe(tap((response) => {
        document.cookie = `token=${response.token}`;
      }))
  }
}
