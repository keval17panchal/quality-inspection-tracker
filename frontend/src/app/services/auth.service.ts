import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { LoginRequest, JwtAuthResponse, User, CreateUserRequest, UpdateUserAdminRequest, Role } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(this.getStoredUser());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<JwtAuthResponse> {
    return this.http.post<JwtAuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((res) => {
        localStorage.setItem('token', res.accessToken);
        const user: User = {
          username: res.username,
          name: res.name,
          role: res.role
        };
        localStorage.setItem('user', JSON.stringify(user));
        this.currentUserSubject.next(user);
      })
    );
  }

  updateProfile(data: { name: string; password?: string }): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/profile`, data).pipe(
      tap((updated) => {
        const currentUser = this.currentUserSubject.value;
        const newUser: User = {
          ...currentUser,
          ...updated
        };
        localStorage.setItem('user', JSON.stringify(newUser));
        this.currentUserSubject.next(newUser);
      })
    );
  }

  // User Management endpoints (Admin only)
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users`);
  }

  createUser(request: CreateUserRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/users`, request);
  }

  updateUser(id: number, request: UpdateUserAdminRequest): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/${id}`, request);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/${id}`);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getStoredUser(): User | null {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch (e) {
        return null;
      }
    }
    return null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getUserRole(): string | null {
    const user = this.getCurrentUser();
    return user ? user.role : null;
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    const role = user?.role ? String(user.role).toUpperCase().trim() : '';
    return role === 'ADMIN' || role === 'ROLE_ADMIN';
  }

  isSupervisor(): boolean {
    const user = this.getCurrentUser();
    const role = user?.role ? String(user.role).toUpperCase().trim() : '';
    return role === 'SUPERVISOR' || role === 'ROLE_SUPERVISOR';
  }

  isInspector(): boolean {
    const user = this.getCurrentUser();
    const role = user?.role ? String(user.role).toUpperCase().trim() : '';
    return role === 'INSPECTOR' || role === 'ROLE_INSPECTOR';
  }

  canCreate(): boolean {
    return this.isAdmin() || this.isSupervisor();
  }

  canEdit(): boolean {
    return this.isAdmin() || this.isSupervisor();
  }

  canResolve(): boolean {
    return this.isAdmin() || this.isSupervisor();
  }

  canDelete(): boolean {
    return this.isAdmin();
  }

  canManageUsers(): boolean {
    return this.isAdmin();
  }

  canUseSapWebhook(): boolean {
    return this.isAdmin() || this.isSupervisor();
  }
}
