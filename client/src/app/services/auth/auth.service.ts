import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

interface requestResponse {
  token: string
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private endpoint = "http://localhost:8080/users"

  constructor(private http: HttpClient) { }

  register(email: String, username: String, password: String): Observable<requestResponse> {
    const data = { email, username, password }

    return this.http.post<requestResponse>(`${this.endpoint}/register`, data)
  }

  login(username: String, password: String): Observable<requestResponse> {
    const data = { username, password }

    return this.http.post<requestResponse>(`${this.endpoint}/login`, data)
  }
}
