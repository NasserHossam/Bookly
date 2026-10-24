import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Order } from '../../shared/models/order.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private baseUrl = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  checkout(): Observable<Order> {
    return this.http.post<Order>(`${this.baseUrl}/checkout`, {});
  }

  getMyOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.baseUrl);
  }

  getById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.baseUrl}/${id}`);
  }
}
