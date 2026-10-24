
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

function passwordsMatchValidator(
  control: AbstractControl
): ValidationErrors | null {

  const password = control.get('password')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;

  return password &&
    confirmPassword &&
    password !== confirmPassword
    ? { mismatch: true }
    : null;
}

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {

  loading = false;

  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toast: ToastService,
    private router: Router
  ) {

    this.form = this.fb.group(
      {
        email: [
          '',
          [Validators.required, Validators.email]
        ],

        phone: [
          '',
          [
            Validators.required,
            Validators.pattern(/^01[0125][0-9]{8}$/)
          ]
        ],

        password: [
          '',
          [
            Validators.required,
            Validators.minLength(6)
          ]
        ],

        confirmPassword: [
          '',
          [Validators.required]
        ]
      },
      {
        validators: passwordsMatchValidator
      }
    );
  }

  submit(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;

    this.authService
      .register(this.form.getRawValue() as any)
      .subscribe({

        next: () => {

          this.loading = false;

          this.toast.success(
            'Account created successfully! Please log in.'
          );

          this.router.navigate(['/login']);
        },

        error: () => {
          this.loading = false;
        }
      });
  }
}

