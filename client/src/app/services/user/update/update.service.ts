import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UpdateService {

  constructor(private http: HttpClient) { }

  update(email: string, username: string, id: string) {
    const url = `http://localhost:8080/users/${id}`;
    const body = { email, username };
    const headers = new HttpHeaders().set('Content-Type', 'application/json');

    return this.http.patch(url, body, { headers, responseType: 'text' })
  }

  updatePass(password: string, id: string) {
    const url = `http://localhost:8080/users/pass/${id}`;
    const body = { password };
    const headers = new HttpHeaders().set('Content-Type', 'application/json');

    return this.http.patch(url, body, { headers, responseType: 'text' })
  }
}
