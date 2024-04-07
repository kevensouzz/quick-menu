import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-generate',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './generate.component.html',
  styleUrl: './generate.component.css'
})
export class GenerateComponent {
  data: any[] = []

  constructor(private http: HttpClient) {
    this.http.get("http://localhost:8080/users")
      .subscribe((user: any) => {
        // this.data = user
      })
  }
}
