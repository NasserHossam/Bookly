import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loading = false;

  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toast: ToastService,
    private router: Router
  ) {
    this.form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.authService.login(this.form.getRawValue() as any).subscribe({
      next: (res) => {
        this.loading = false;
        this.toast.success('Welcome back!');
        this.router.navigate([res.role === 'ADMIN' ? '/admin/books' : '/home']);
      },
      error: () => {
        this.loading = false;
      }
    });
  }
}
