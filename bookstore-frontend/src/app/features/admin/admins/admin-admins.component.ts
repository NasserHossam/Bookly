import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminService } from '../../../core/services/admin.service';
import { ToastService } from '../../../core/services/toast.service';
import { User } from '../../../shared/models/user.model';

@Component({
  selector: 'app-admin-admins',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-admins.component.html',
  styleUrl: './admin-admins.component.css'
})
export class AdminAdminsComponent implements OnInit {
  admins: User[] = [];
  loading = true;
  saving = false;

  showModal = false;

  showDeleteConfirm = false;
  adminToDelete: User | null = null;
  deleting = false;

  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private toast: ToastService
  ) {
    this.form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required, Validators.pattern(/^01[0125][0-9]{8}$/)]],
    password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    this.loadAdmins();
  }

  loadAdmins(): void {
    this.loading = true;
    this.adminService.getAll().subscribe({
      next: (admins) => {
        this.admins = admins;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  openAddModal(): void {
    this.form.reset({ email: '', phone: '', password: '' });
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.adminService.create(this.form.getRawValue() as any).subscribe({
      next: () => {
        this.saving = false;
        this.showModal = false;
        this.toast.success('Admin created successfully');
        this.loadAdmins();
      },
      error: () => {
        this.saving = false;
      }
    });
  }

  confirmDelete(admin: User): void {
    this.adminToDelete = admin;
    this.showDeleteConfirm = true;
  }

  cancelDelete(): void {
    this.showDeleteConfirm = false;
    this.adminToDelete = null;
  }

  deleteAdmin(): void {
    if (!this.adminToDelete) return;
    this.deleting = true;

    this.adminService.delete(this.adminToDelete.id).subscribe({
      next: () => {
        this.deleting = false;
        this.showDeleteConfirm = false;
        this.toast.success('Admin deleted successfully');
        this.loadAdmins();
      },
      error: () => {
        this.deleting = false;
      }
    });
  }
}
