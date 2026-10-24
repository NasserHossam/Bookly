import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { BookService } from '../../../core/services/book.service';
import { ToastService } from '../../../core/services/toast.service';
import { Book } from '../../../shared/models/book.model';

@Component({
  selector: 'app-admin-books',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-books.component.html',
  styleUrl: './admin-books.component.css'
})
export class AdminBooksComponent implements OnInit {
  books: Book[] = [];
  loading = true;
  saving = false;

  showModal = false;
  editingBook: Book | null = null;

  showDeleteConfirm = false;
  bookToDelete: Book | null = null;
  deleting = false;

  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private bookService: BookService,
    private toast: ToastService
  ) {
    this.form = this.fb.group({
    title: ['', [Validators.required]],
    author: ['', [Validators.required]],
    category: ['', [Validators.required]],
    price: [0, [Validators.required, Validators.min(0.01)]],
    description: ['', [Validators.required]],
    imageUrl: ['']
    });
  }

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.loading = true;
    this.bookService.getAll().subscribe({
      next: (books) => {
        this.books = books;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  openAddModal(): void {
    this.editingBook = null;
    this.form.reset({ title: '', author: '', category: '', price: 0, description: '', imageUrl: '' });
    this.showModal = true;
  }

  openEditModal(book: Book): void {
    this.editingBook = book;
    this.form.reset(book);
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    const payload = this.form.getRawValue() as any;

    const request$ = this.editingBook
      ? this.bookService.update(this.editingBook.id, payload)
      : this.bookService.create(payload);

    request$.subscribe({
      next: () => {
        this.saving = false;
        this.showModal = false;
        this.toast.success(this.editingBook ? 'Book updated successfully' : 'Book added successfully');
        this.loadBooks();
      },
      error: () => {
        this.saving = false;
      }
    });
  }

  confirmDelete(book: Book): void {
    this.bookToDelete = book;
    this.showDeleteConfirm = true;
  }

  cancelDelete(): void {
    this.showDeleteConfirm = false;
    this.bookToDelete = null;
  }

  deleteBook(): void {
    if (!this.bookToDelete) return;
    this.deleting = true;

    this.bookService.delete(this.bookToDelete.id).subscribe({
      next: () => {
        this.deleting = false;
        this.showDeleteConfirm = false;
        this.toast.success('Book deleted successfully');
        this.loadBooks();
      },
      error: () => {
        this.deleting = false;
      }
    });
  }
}
