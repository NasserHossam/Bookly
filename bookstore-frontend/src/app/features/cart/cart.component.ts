import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../core/services/cart.service';
import { OrderService } from '../../core/services/order.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  loading = true;
  checkingOut = false;
  updatingItemId: number | null = null;

  constructor(
    public cartService: CartService,
    private orderService: OrderService,
    private toast: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.cartService.refresh();
    // The service call is fire-and-forget for the signal; stop the spinner
    // on the next tick once the signal has had a chance to populate.
    setTimeout(() => (this.loading = false), 300);
  }

  get total(): number {
    return this.cartService.items().reduce((sum, item) => sum + item.subtotal, 0);
  }

  increment(itemId: number, currentQty: number): void {
    this.changeQuantity(itemId, currentQty + 1);
  }

  decrement(itemId: number, currentQty: number): void {
    if (currentQty <= 1) {
      return;
    }
    this.changeQuantity(itemId, currentQty - 1);
  }

  private changeQuantity(itemId: number, quantity: number): void {
    this.updatingItemId = itemId;
    this.cartService.updateQuantity(itemId, { quantity }).subscribe({
      next: () => (this.updatingItemId = null),
      error: () => {
        this.updatingItemId = null;
        this.toast.error('Could not update quantity');
      }
    });
  }

  removeItem(itemId: number): void {
    this.cartService.remove(itemId).subscribe({
      error: () => this.toast.error('Could not remove item')
    });
  }

  checkout(): void {
    this.checkingOut = true;
    this.orderService.checkout().subscribe({
      next: () => {
        this.checkingOut = false;
        this.cartService.clear();
        this.toast.success('Order placed! Check My Purchases for the receipt.');
        this.router.navigate(['/purchases']);
      },
      error: (err) => {
        this.checkingOut = false;
        this.toast.error(err?.error?.message || 'Checkout failed');
      }
    });
  }
}
