import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CartItem, CartItemRequest, UpdateCartItemRequest } from '../../shared/models/cart.model';

@Injectable({ providedIn: 'root' })
export class CartService {
  private baseUrl = `${environment.apiUrl}/cart`;

  // Reactive cart state so the navbar badge and cart page stay in sync
  // without every component re-fetching independently.
  items = signal<CartItem[]>([]);
  itemCount = computed(() =>
    this.items().reduce((sum, item) => sum + item.quantity, 0)
  );

  constructor(private http: HttpClient) {}

  refresh(): void {
    this.http.get<CartItem[]>(this.baseUrl).subscribe({
      next: (items) => this.items.set(items),
      error: () => this.items.set([])
    });
  }

  add(request: CartItemRequest): Observable<CartItem> {
    return this.http.post<CartItem>(this.baseUrl, request)
      .pipe(tap(() => this.refresh()));
  }

  updateQuantity(itemId: number, request: UpdateCartItemRequest): Observable<CartItem> {
    return this.http.put<CartItem>(`${this.baseUrl}/${itemId}`, request)
      .pipe(tap(() => this.refresh()));
  }

  remove(itemId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${itemId}`)
      .pipe(tap(() => this.refresh()));
  }

  clear(): void {
    this.items.set([]);
  }
}
