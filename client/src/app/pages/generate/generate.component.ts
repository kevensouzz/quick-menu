import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CookiesService } from '../../services/cookies/cookies.service';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';

interface ExtendedJwtPayload extends JwtPayload {
  id?: string;
}

interface Option {
  name: string;
  description: string;
  picture: Uint8Array;
  price: number;
  avaliability: boolean;
}

interface Menu {
  menuId: string;
  name: string;
  code: string;
  options: Option[];
}

@Component({
  selector: 'app-generate',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './generate.component.html',
  styleUrl: './generate.component.css'
})
export class GenerateComponent {
  token: string | null | undefined;
  userId: string | undefined;
  menus: Menu[] = [];

  selectedMenu: Menu | null = null;

  confirmDelete: boolean = false;

  constructor(
    private http: HttpClient,
    private cookie: CookiesService,
    private toast: ToastrService
  ) {
    this.loadUser()
  }

  loadUser() {
    this.token = this.cookie.get("token")

    if (this.token) {
      this.userId = jwtDecode<ExtendedJwtPayload>(this.token).id;
    }

    return this.http.get(`http://localhost:8080/users/${this.userId}`, {
      headers: {
        Authorization: `Bearer ${this.token}`
      }
    }).subscribe((data: any) => {
      this.menus = data.menus

    })
  }

  deleteMenu(menuId: string | undefined) {
    return this.http.delete(`http://localhost:8080/menus/${menuId}`, {
      headers: {
        Authorization: `Bearer ${this.token}`
      }
    })
      .subscribe({
        error: () => {
          this.toast.error("Successfully Deleted!")
        }
      })
  }
}
