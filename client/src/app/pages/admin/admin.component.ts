import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  data: any[] = []

  constructor(private http: HttpClient) {
    this.loadUsers()
  }

  loadUsers() {
    this.http.get("http://localhost:8080/users")
      .subscribe((users: any) => {
        this.data = users
      })
  }

}
