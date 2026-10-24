import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest, Role, User } from '../../shared/models/user.model';

const TOKEN_KEY = 'bookly_token';
const ROLE_KEY = 'bookly_role';
const EMAIL_KEY = 'bookly_email';
const NAME_KEY = 'bookly_name';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private authUrl = `${environment.apiUrl}/auth`;
  private usersUrl = `${environment.apiUrl}/users`;

  // Reactive signals so navbar/guards can react to auth state instantly
  isAuthenticated = signal<boolean>(!!localStorage.getItem(TOKEN_KEY));
  currentEmail = signal<string | null>(localStorage.getItem(EMAIL_KEY));
  currentName = signal<string | null>(localStorage.getItem(NAME_KEY));
  currentRole = signal<Role | null>(localStorage.getItem(ROLE_KEY) as Role | null);

  constructor(private http: HttpClient) {}

  // Backend endpoint is POST /api/users/register and does NOT return a token,
  // so registration does not auto-login — the user is redirected to /login instead.
  // confirmPassword is stripped out since the backend DTO doesn't have that field.
  register(request: RegisterRequest): Observable<User> {
    const { confirmPassword, ...payload } = request;
    return this.http.post<User>(`${this.usersUrl}/register`, payload);
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.authUrl}/login`, request)
      .pipe(tap((res) => this.setSession(res)));
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
    localStorage.removeItem(EMAIL_KEY);
    localStorage.removeItem(NAME_KEY);
    this.isAuthenticated.set(false);
    this.currentEmail.set(null);
    this.currentName.set(null);
    this.currentRole.set(null);
  }

  // Called after a profile edit so the navbar/greeting reflect the new
  // name/email right away without requiring a re-login.
  updateSessionProfile(name: string | null, email: string): void {
    localStorage.setItem(EMAIL_KEY, email);
    if (name) {
      localStorage.setItem(NAME_KEY, name);
    } else {
      localStorage.removeItem(NAME_KEY);
    }
    this.currentEmail.set(email);
    this.currentName.set(name);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getRole(): Role | null {
    return localStorage.getItem(ROLE_KEY) as Role | null;
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  private setSession(res: AuthResponse): void {
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(ROLE_KEY, res.role);
    localStorage.setItem(EMAIL_KEY, res.email);
    if (res.name) {
      localStorage.setItem(NAME_KEY, res.name);
    }
    this.isAuthenticated.set(true);
    this.currentEmail.set(res.email);
    this.currentName.set(res.name);
    this.currentRole.set(res.role);
  }
}
