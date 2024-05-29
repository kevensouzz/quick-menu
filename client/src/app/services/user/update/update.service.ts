import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookiesService } from '../../cookies/cookies.service';

@Injectable({
  providedIn: 'root'
})
export class UpdateService {

  constructor(
    private http: HttpClient,
    private cookie: CookiesService
  ) { }

  update(email: string, username: string, id: string) {
    const url = `http://localhost:8080/users/${id}`;
    const body = { email, username };

    return this.http.patch(url, body, {
      headers: {
        "Content-Type": "application/json",
        responseType: "text",
        Authorization: `Bearer ${this.cookie.get("token")}`
      }
    })
  }

  updatePass(password: string, id: string) {
    const url = `http://localhost:8080/users/password/${id}`;
    const body = { password };

    return this.http.patch(url, body, {
      headers: {
        "Content-Type": "application/json",
        responseType: "text",
        Authorization: `Bearer ${this.cookie.get("token")}`
      }
    })
  }

  updateRole(role: string, id: string) {
    const url = `http://localhost:8080/users/role/${id}`;
    const body = { role };

    return this.http.patch(url, body, {
      headers: {
        "Content-Type": "application/json",
        responseType: "text",
        Authorization: `Bearer ${this.cookie.get("token")}`
      }
    })
  }
}
