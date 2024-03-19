import { Component } from '@angular/core';
import { NavComponent } from './nav/nav.component';
import { BannerComponent } from './banner/banner.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [NavComponent, BannerComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

}
