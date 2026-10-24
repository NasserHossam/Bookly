import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AdminCreateRequest, User } from '../../shared/models/user.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private baseUrl = `${environment.apiUrl}/admins`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl);
  }

  create(admin: AdminCreateRequest): Observable<User> {
    return this.http.post<User>(this.baseUrl, admin);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
