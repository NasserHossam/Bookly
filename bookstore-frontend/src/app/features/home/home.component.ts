import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BookService } from '../../core/services/book.service';
import { Book } from '../../shared/models/book.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  books: Book[] = [];
  loading = true;

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.bookService.getAll().subscribe({
      next: (books) => {
        this.books = books.slice(0, 4);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }
}
