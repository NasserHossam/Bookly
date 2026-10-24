import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'signup',
    loadComponent: () => import('./features/auth/signup/signup.component').then(m => m.SignupComponent)
  },

  {
    path: 'home',
    loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'books',
    loadComponent: () => import('./features/books/book-list/book-list.component').then(m => m.BookListComponent)
  },
  {
    path: 'books/:id',
    loadComponent: () => import('./features/books/book-details/book-details.component').then(m => m.BookDetailsComponent)
  },

  {
    path: 'cart',
    canActivate: [authGuard],
    loadComponent: () => import('./features/cart/cart.component').then(m => m.CartComponent)
  },
  {
    path: 'purchases',
    canActivate: [authGuard],
    loadComponent: () => import('./features/purchases/purchases.component').then(m => m.PurchasesComponent)
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent)
  },

  {
    path: 'admin/books',
    canActivate: [authGuard, adminGuard],
    loadComponent: () => import('./features/admin/books/admin-books.component').then(m => m.AdminBooksComponent)
  },
  {
    path: 'admin/admins',
    canActivate: [authGuard, adminGuard],
    loadComponent: () => import('./features/admin/admins/admin-admins.component').then(m => m.AdminAdminsComponent)
  },

  { path: '**', redirectTo: 'home' }
];
