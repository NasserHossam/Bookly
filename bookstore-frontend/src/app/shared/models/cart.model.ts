import { Book } from './book.model';

export interface CartItem {
  id: number;
  book: Book;
  quantity: number;
  subtotal: number;
}

export interface CartItemRequest {
  bookId: number;
  quantity: number;
}

export interface UpdateCartItemRequest {
  quantity: number;
}
