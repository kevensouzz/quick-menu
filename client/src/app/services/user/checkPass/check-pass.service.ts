import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookiesService } from '../../cookies/cookies.service';

@Injectable({
  providedIn: 'root'
})
export class CheckPassService {

  constructor(
    private http: HttpClient,
    private cookie: CookiesService
  ) { }

  checkPass(password: string, id: string) {
    const url = `http://localhost:8080/users/check-password/${id}`;
    const body = { password };

    return this.http.post(url, body, {
      headers: {
        "Content-Type": "application/json",
        responseType: "text",
        Authorization: `Bearer ${this.cookie.get("token")}`
      }
    })
  }
}
