import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { ToastService } from '../services/toast.service';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toast = inject(ToastService);
  const router = inject(Router);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const serverMessage = error.error?.message;

      if (error.status === 401) {
        toast.error(serverMessage || 'Invalid email or password');
        if (authService.getToken()) {
          authService.logout();
          router.navigate(['/login']);
        }
      } else if (error.status === 403) {
        toast.error('You are not authorized to perform this action');
      } else if (error.status === 404) {
        toast.error(serverMessage || 'Not found');
      } else if (error.status === 409) {
        toast.error(serverMessage || 'Conflict occurred');
      } else if (error.status === 0) {
        toast.error('Cannot reach the server');
      } else {
        toast.error(serverMessage || 'Something went wrong');
      }

      return throwError(() => error);
    })
  );
};
