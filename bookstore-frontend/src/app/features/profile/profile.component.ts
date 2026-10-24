import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../core/services/user.service';
import { ToastService } from '../../core/services/toast.service';

function passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
  const newPassword = control.get('newPassword')?.value;
  const confirmNewPassword = control.get('confirmNewPassword')?.value;
  return newPassword && confirmNewPassword && newPassword !== confirmNewPassword
    ? { mismatch: true }
    : null;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  loading = true;
  savingProfile = false;
  savingPassword = false;

  profileForm!: FormGroup;
  passwordForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private toast: ToastService
  ) {
    this.profileForm = this.fb.group({
      name: [''],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^01[0125][0-9]{8}$/)]]
    });

    this.passwordForm = this.fb.group(
      {
        currentPassword: ['', [Validators.required]],
        newPassword: ['', [Validators.required, Validators.minLength(6)]],
        confirmNewPassword: ['', [Validators.required]]
      },
      { validators: passwordsMatchValidator }
    );
  }

  ngOnInit(): void {
    this.userService.getMe().subscribe({
      next: (user) => {
        this.profileForm.patchValue({
          name: user.name || '',
          email: user.email,
          phone: user.phone
        });
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  saveProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    this.savingProfile = true;
    this.userService.updateProfile(this.profileForm.getRawValue()).subscribe({
      next: (user) => {
        this.savingProfile = false;
        this.authService.updateSessionProfile(user.name, user.email);
        this.toast.success('Profile updated successfully');
      },
      error: (err) => {
        this.savingProfile = false;
        this.toast.error(err?.error?.message || 'Could not update profile');
      }
    });
  }

  savePassword(): void {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    this.savingPassword = true;
    const { currentPassword, newPassword } = this.passwordForm.getRawValue();

    this.userService.changePassword({ currentPassword, newPassword }).subscribe({
      next: () => {
        this.savingPassword = false;
        this.passwordForm.reset();
        this.toast.success('Password changed successfully');
      },
      error: (err) => {
        this.savingPassword = false;
        this.toast.error(err?.error?.message || 'Could not change password');
      }
    });
  }
}
