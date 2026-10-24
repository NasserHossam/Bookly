import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ChangePasswordRequest, UpdateProfileRequest, User } from '../../shared/models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getMe(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/me`);
  }

  updateProfile(request: UpdateProfileRequest): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/me`, request);
  }

  changePassword(request: ChangePasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/me/password`, request);
  }
}
