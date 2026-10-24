export interface OrderItem {
  bookId: number | null;
  bookTitle: string;
  bookAuthor: string;
  bookImageUrl: string | null;
  unitPrice: number;
  quantity: number;
  lineTotal: number;
}

export interface Order {
  id: number;
  orderDate: string;
  totalAmount: number;
  status: string;
  items: OrderItem[];
}
