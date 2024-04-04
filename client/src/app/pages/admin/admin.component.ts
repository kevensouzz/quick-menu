import { NgOptimizedImage } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [NgOptimizedImage],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent{

  selectedOption: string = 'all';
  users: any[] = []
  usersData: any[] = []
  searchTerm: string = ''

  constructor(private http: HttpClient) {
    this.loadUsers()
  }

  userFilter(selectedOption: string) {
    let filteredUsers = this.usersData;

    switch (selectedOption) {
      case "all":
        filteredUsers = this.usersData;
        break;
      case "user":
        filteredUsers = this.usersData.filter(user => user.role === 'USER');
        break;
      case "admin":
        filteredUsers = this.usersData.filter(user => user.role === 'ADMIN');
        break;
    }

    filteredUsers = filteredUsers.filter(user => 
      user.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.username.toLowerCase().includes(this.searchTerm.toLowerCase())
    );

    this.selectedOption = selectedOption
    this.users = filteredUsers;
  }

  onSearchChange(event: any) {
    this.searchTerm = event.target.value;
    this.userFilter(this.selectedOption);
 }

  loadUsers() {
    this.http.get("http://localhost:8080/users")
      .subscribe((users: any) => {
        this.usersData = users
        this.userFilter('all')
      })
  }
}
