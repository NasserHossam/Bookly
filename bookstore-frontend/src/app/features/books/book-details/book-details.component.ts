import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BookService } from '../../../core/services/book.service';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { Book } from '../../../shared/models/book.model';

@Component({
  selector: 'app-book-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './book-details.component.html',
  styleUrl: './book-details.component.css'
})
export class BookDetailsComponent implements OnInit {
  book: Book | null = null;
  loading = true;
  notFound = false;
  quantity = 1;
  addingToCart = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bookService: BookService,
    private cartService: CartService,
    private authService: AuthService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.bookService.getById(id).subscribe({
      next: (book) => {
        this.book = book;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.notFound = true;
      }
    });
  }

  incrementQty(): void {
    this.quantity++;
  }

  decrementQty(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart(): void {
    if (!this.book) {
      return;
    }

    if (!this.authService.isAuthenticated()) {
      this.toast.error('Please log in to add items to your cart');
      this.router.navigate(['/login']);
      return;
    }

    this.addingToCart = true;
    this.cartService.add({ bookId: this.book.id, quantity: this.quantity }).subscribe({
      next: () => {
        this.addingToCart = false;
        this.toast.success(`${this.book!.title} added to cart`);
      },
      error: (err) => {
        this.addingToCart = false;
        this.toast.error(err?.error?.message || 'Could not add to cart');
      }
    });
  }

  hideBrokenImage(event: Event): void {
    (event.target as HTMLImageElement).style.display = 'none';
  }
}
