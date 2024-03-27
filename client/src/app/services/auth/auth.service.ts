import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

interface authResponse {
  token: string,
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private endpoint = "http://localhost:8080/users"

  private setCookie(name: string, value: string, expireHours: number, path: string = '') {
    let d: Date = new Date();
    d.setTime(d.getTime() + expireHours * 60 * 60 * 1000);
    let expires: string = `expires=${d.toUTCString()}`;
    let cpath: string = path ? `path=${path}` : ''
    document.cookie = `${name}=${value}; ${expires}; ${cpath}`;
  }

  constructor(private http: HttpClient, private router: Router) { }

  register(email: String, username: String, password: String) {
    return this.http.post<authResponse>(`${this.endpoint}/register`, { email, username, password })
      .pipe(tap(Response => {
        this.setCookie("token", Response.token, 2, "/")
        window.location.reload()
      }))
  }

  login(username: String, password: String) {
    return this.http.post<authResponse>(`${this.endpoint}/login`, { username, password })
      .pipe(tap(Response => {
        this.setCookie("token", Response.token, 2, "/")
        window.location.reload()
      }))
  }
}
