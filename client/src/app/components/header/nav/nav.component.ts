import { Component, Injectable } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { BannerComponent } from '../banner/banner.component';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, NgOptimizedImage, BannerComponent],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {
  isMobileMenuOpen: boolean = false

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen
  }
}