import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { User, Role, CreateUserRequest, UpdateUserAdminRequest } from '../../models/user.model';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  // Modals
  isModalOpen = false;
  isEditing = false;
  selectedUser: User | null = null;
  userForm: FormGroup;
  isSubmitting = false;

  // Delete Confirmation Modal
  isDeleteModalOpen = false;
  userToDelete: User | null = null;
  isDeleting = false;

  roles = Object.values(Role);

  constructor(
    public authService: AuthService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      name: ['', [Validators.required, Validators.minLength(2)]],
      role: [Role.SUPERVISOR, [Validators.required]],
      password: ['']
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.authService.getAllUsers().subscribe({
      next: (data: User[]) => {
        this.users = Array.isArray(data) ? data : [];
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        this.errorMessage = err.error?.message || 'Failed to load users.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  openCreateModal(): void {
    this.isEditing = false;
    this.selectedUser = null;
    this.userForm.reset({
      username: '',
      name: '',
      role: Role.SUPERVISOR,
      password: ''
    });
    this.userForm.get('username')?.enable();
    this.userForm.get('password')?.setValidators([Validators.required, Validators.minLength(4)]);
    this.userForm.get('password')?.updateValueAndValidity();
    this.isModalOpen = true;
    this.clearMessages();
  }

  openEditModal(user: User): void {
    this.isEditing = true;
    this.selectedUser = user;
    this.userForm.reset({
      username: user.username,
      name: user.name,
      role: user.role,
      password: ''
    });
    this.userForm.get('username')?.disable();
    this.userForm.get('password')?.clearValidators();
    this.userForm.get('password')?.updateValueAndValidity();
    this.isModalOpen = true;
    this.clearMessages();
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.selectedUser = null;
    this.userForm.reset();
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.clearMessages();

    if (this.isEditing && this.selectedUser?.id) {
      const updateData: UpdateUserAdminRequest = {
        name: this.userForm.value.name,
        role: this.userForm.value.role,
        password: this.userForm.value.password || undefined
      };

      this.authService.updateUser(this.selectedUser.id, updateData).subscribe({
        next: () => {
          this.isSubmitting = false;
          this.successMessage = `User "${this.selectedUser?.username}" updated successfully.`;
          this.closeModal();
          this.loadUsers();
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.isSubmitting = false;
          this.errorMessage = err.error?.message || 'Failed to update user.';
          this.cdr.detectChanges();
        }
      });
    } else {
      const createData: CreateUserRequest = {
        username: this.userForm.get('username')?.value,
        name: this.userForm.value.name,
        role: this.userForm.value.role,
        password: this.userForm.value.password
      };

      this.authService.createUser(createData).subscribe({
        next: (created) => {
          this.isSubmitting = false;
          this.successMessage = `User "${created.username}" created successfully.`;
          this.closeModal();
          this.loadUsers();
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.isSubmitting = false;
          this.errorMessage = err.error?.message || 'Failed to create user.';
          this.cdr.detectChanges();
        }
      });
    }
  }

  openDeleteModal(user: User): void {
    this.userToDelete = user;
    this.isDeleteModalOpen = true;
    this.clearMessages();
  }

  closeDeleteModal(): void {
    this.isDeleteModalOpen = false;
    this.userToDelete = null;
  }

  confirmDelete(): void {
    if (!this.userToDelete?.id) return;

    this.isDeleting = true;
    this.authService.deleteUser(this.userToDelete.id).subscribe({
      next: () => {
        this.isDeleting = false;
        this.successMessage = `User "${this.userToDelete?.username}" deleted successfully.`;
        this.closeDeleteModal();
        this.loadUsers();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isDeleting = false;
        this.errorMessage = err.error?.message || 'Failed to delete user.';
        this.closeDeleteModal();
        this.cdr.detectChanges();
      }
    });
  }

  clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  getRoleBadgeClass(role: Role | string): string {
    switch (role) {
      case Role.ADMIN:
      case 'ADMIN':
        return 'badge-admin';
      case Role.SUPERVISOR:
      case 'SUPERVISOR':
        return 'badge-supervisor';
      case Role.INSPECTOR:
      case 'INSPECTOR':
        return 'badge-inspector';
      default:
        return 'badge-default';
    }
  }
}
