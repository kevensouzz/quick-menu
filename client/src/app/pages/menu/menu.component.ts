import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

interface Menu {
  options: Option[];
}

interface Option {
  name: string;
  description: string;
  picture: Uint8Array;
  price: number;
  avaliability: boolean;
}

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent {
  code!: string
  menuFound: boolean = true
  menu: Menu | undefined
  options: Option[] | undefined

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {
    this.code = route.snapshot.params["code"]

    http.get(`http://localhost:8080/menus/code/${this.code}`)
      .subscribe({
        next: (data: any) => {
          this.menu = data
          this.options = this.menu?.options
        },
        error: () => {
          return this.menuFound = false
        }
      })
  }
}
