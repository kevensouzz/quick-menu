import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-scan',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './scan.component.html',
  styleUrl: './scan.component.css'
})
export class ScanComponent {
  menus: any[] = []
  menusData: any[] = []
  searchTerm: string = ''

  constructor(
    private http: HttpClient
  ) {
    this.loadMenus()
  }

  menuFilter() {
    let filteredMenus = this.menusData

    filteredMenus = filteredMenus.filter(menu =>
      menu.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      menu.code.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      menu.user.username.toLowerCase().includes(this.searchTerm.toLowerCase())
    );

    this.menus = filteredMenus;
  }

  onSearchChange(event: any) {
    this.searchTerm = event.target.value;
    this.menuFilter();
  }

  loadMenus() {
    this.http.get("http://localhost:8080/menus")
      .subscribe((menus: any) => {
        this.menusData = menus
        this.menuFilter()
      })
  }
}
