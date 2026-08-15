export enum Role {
  ADMIN = 'ADMIN',
  SUPERVISOR = 'SUPERVISOR',
  INSPECTOR = 'INSPECTOR'
}

export interface User {
  id?: number;
  username: string;
  name: string;
  role: Role | string;
}

export interface CreateUserRequest {
  username: string;
  password: string;
  name: string;
  role: Role | string;
}

export interface UpdateUserAdminRequest {
  name: string;
  role: Role | string;
  password?: string;
}

export interface LoginRequest {
  username: string;
  password?: string;
}

export interface JwtAuthResponse {
  accessToken: string;
  tokenType: string;
  username: string;
  name: string;
  role: Role | string;
}
