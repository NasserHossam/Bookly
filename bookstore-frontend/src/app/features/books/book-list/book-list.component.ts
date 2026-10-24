import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { BookService } from '../../../core/services/book.service';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { Book } from '../../../shared/models/book.model';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.css'
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  filteredBooks: Book[] = [];
  categories: string[] = [];
  loading = true;
  addingBookId: number | null = null;

  searchTerm = '';
  selectedCategory = 'All';

  constructor(
    private bookService: BookService,
    private cartService: CartService,
    private authService: AuthService,
    private toast: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.bookService.getAll().subscribe({
      next: (books) => {
        this.books = books;
        this.categories = ['All', ...new Set(books.map((b) => b.category))];
        this.applyFilters();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    this.filteredBooks = this.books.filter((book) => {
      const matchesSearch =
        book.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        book.author.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesCategory = this.selectedCategory === 'All' || book.category === this.selectedCategory;
      return matchesSearch && matchesCategory;
    });
  }

  hideBrokenImage(event: Event): void {
    (event.target as HTMLImageElement).style.display = 'none';
  }

  quickAddToCart(event: Event, book: Book): void {
    event.preventDefault();
    event.stopPropagation();

    if (!this.authService.isAuthenticated()) {
      this.toast.error('Please log in to add items to your cart');
      this.router.navigate(['/login']);
      return;
    }

    this.addingBookId = book.id;
    this.cartService.add({ bookId: book.id, quantity: 1 }).subscribe({
      next: () => {
        this.addingBookId = null;
        this.toast.success(`${book.title} added to cart`);
      },
      error: (err) => {
        this.addingBookId = null;
        this.toast.error(err?.error?.message || 'Could not add to cart');
      }
    });
  }
}
