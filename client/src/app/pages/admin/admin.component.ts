import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { UpdateService } from '../../services/user/update/update.service';
import { CommonModule } from '@angular/common';
import { CookiesService } from '../../services/cookies/cookies.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  users: any[] = []
  usersData: any[] = []

  selectedOption: string = 'all'
  searchTerm: string = ''

  userModal: boolean = false

  selectedUser: any = null

  modalButtons: boolean = true
  editUser: boolean = false
  editUserPass: boolean = false
  confirmDelete: boolean = false

  update!: FormGroup
  updatePass!: FormGroup

  constructor(
    private service: UpdateService,
    private http: HttpClient,
    private toast: ToastrService,
    private cookie: CookiesService,
  ) {
    this.loadUsers()
    console.clear()

    this.update = new FormGroup({
      email: new FormControl("", [Validators.required, Validators.email]),
      username: new FormControl("", [Validators.required, Validators.minLength(3), Validators.maxLength(16)]),
    })

    this.updatePass = new FormGroup({
      password: new FormControl("", [Validators.required, Validators.minLength(8)])
    })
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
    this.http.get("http://localhost:8080/users", {
      headers: {
        "Authorization": `Bearer ${this.cookie.get("token")}`
      }
    })
      .subscribe((users: any) => {
        this.usersData = users
        this.userFilter('all')
      })
  }

  closeUserModal() {
    this.userModal = false
    this.selectedUser = null

    this.modalButtons = true
    this.editUser = false
    this.editUserPass = false
    this.confirmDelete = false
  }

  updateSubmit(id: string) {

    if (this.selectedUser?.role == "ADMIN") {
      this.toast.error("Can Not Touch An Admin!")
    }

    if (this.update.value.email == this.selectedUser?.email) {
      this.update.value.email = null
    }

    if (this.update.value.username == this.selectedUser?.username) {
      this.update.value.username = null
    }

    if (this.update.valid) {
      this.service.update(this.update.value.email, this.update.value.username, id)
        .subscribe({
          next: () => {
            this.toast.error("Successfully Updated!")
          },
          error: () => {
            this.toast.error("Something Went Wrong!")
          },
        })
    }
  }

  updatePassSubmit(id: string) {
    if (this.selectedUser?.role == "ADMIN") {
      this.toast.error("Can Not Touch An Admin!")
    }

    if (this.updatePass.valid) {
      this.service.updatePass(this.updatePass.value.password, id)
        .subscribe({
          next: () => {
            this.toast.error("Successfully Updated!")
          },
          error: () => {
            this.toast.error("Something Went Wrong!")
          },
        })
    }
  }

  deleteUser(id: string) {
    if (this.selectedUser?.role == "ADMIN") {
      return this.toast.error("Can Not Touch An Admin!")
    }

    return this.http.delete(`http://localhost:8080/users/${id}`, {
      headers: {
        "Authorization": `Bearer ${this.cookie.get("token")}`
      }
    })
      .subscribe({
        error: () => {
          this.toast.error("Successfully Deleted!")
        }
      })
  }
}
