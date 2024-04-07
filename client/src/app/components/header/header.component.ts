import { Component, HostListener, OnInit } from '@angular/core';
import { LinkComponent } from './link/link.component';
import { CommonModule } from '@angular/common';
import { BannerComponent } from './banner/banner.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [BannerComponent, LinkComponent, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  mobileMenu: boolean = false
  screenWidth: any;

  toggleMobileMenu() {
    this.mobileMenu = !this.mobileMenu
  }

  ngOnInit(): void {
    this.screenWidth = window.innerWidth
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.screenWidth = window.innerWidth;
  }

}
