import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-new-menu',
  standalone: true,
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './new-menu.component.html',
  styleUrl: './new-menu.component.css'
})
export class NewMenuComponent {
  newMenu!: FormGroup;

  constructor(
    private toast: ToastrService,
    private http: HttpClient
  ) {
    this.http.get("http://localhost:8080/menus")
      .subscribe()

    this.newMenu = new FormGroup({
      name: new FormControl("", [Validators.required]),
      code: new FormControl("", [Validators.required, Validators.minLength(6), Validators.maxLength(6)]),
    })
  }

  newMenuSubmit() {
    if (this.newMenu.valid) {

      const formValues = this.newMenu.value;
      const name = formValues.name;
      const code = formValues.code.toUpperCase();

      this.http.post("http://localhost:8080/menus", { name, code })
        .subscribe({
          next: () => {
            this.newMenu.reset()
            this.toast.error("Successfully Created!")
          },
          error: () => {
            this.newMenu.reset()
            this.toast.error("Somenthing Wrong!")
          }
        })
    }
  }
}
