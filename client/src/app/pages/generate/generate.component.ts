import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-generate',
  standalone: true,
  imports: [],
  templateUrl: './generate.component.html',
  styleUrl: './generate.component.css'
})
export class GenerateComponent {
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
