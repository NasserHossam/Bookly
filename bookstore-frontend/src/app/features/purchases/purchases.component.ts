import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../core/services/order.service';
import { Order } from '../../shared/models/order.model';

@Component({
  selector: 'app-purchases',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './purchases.component.html',
  styleUrl: './purchases.component.css'
})
export class PurchasesComponent implements OnInit {
  orders: Order[] = [];
  loading = true;
  expandedOrderId: number | null = null;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getMyOrders().subscribe({
      next: (orders) => {
        this.orders = orders;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  toggleExpand(orderId: number): void {
    this.expandedOrderId = this.expandedOrderId === orderId ? null : orderId;
  }
}
