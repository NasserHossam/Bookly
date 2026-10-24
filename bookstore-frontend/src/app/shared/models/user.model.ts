export type Role = 'USER' | 'ADMIN';

export interface User {
  id: number;
  name: string | null;
  email: string;
  phone: string;
  role: Role;
}

// Matches backend LoginResponse — note: no id field is returned on login
export interface AuthResponse {
  token: string;
  email: string;
  name: string | null;
  role: Role;
}

export interface UpdateProfileRequest {
  name: string;
  email: string;
  phone: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  confirmPassword: string;
  phone: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AdminCreateRequest {
  email: string;
  password: string;
  phone: string;
}
