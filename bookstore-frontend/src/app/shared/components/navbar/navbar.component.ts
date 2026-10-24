import { Component, OnInit, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { CartService } from '../../../core/services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  menuOpen = false;

  constructor(
    public authService: AuthService,
    public cartService: CartService,
    private router: Router
  ) {
    // Keep the cart badge in sync whenever auth state flips (login/logout).
    effect(() => {
      if (this.authService.isAuthenticated()) {
        this.cartService.refresh();
      } else {
        this.cartService.clear();
      }
    });
  }

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.cartService.refresh();
    }
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  logout(): void {
    this.authService.logout();
    this.cartService.clear();
    this.menuOpen = false;
    this.router.navigate(['/login']);
  }
}
