export interface Book {
  id: number;
  title: string;
  author: string;
  category: string;
  price: number;
  description: string;
  imageUrl: string;
}

export type BookRequest = Omit<Book, 'id'>;
