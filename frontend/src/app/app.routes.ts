import { Routes } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CreateInspectionComponent } from './components/create-inspection/create-inspection.component';
import { InspectionListComponent } from './components/inspection-list/inspection-list.component';
import { SapMockComponent } from './components/sap-mock/sap-mock.component';
import { LoginComponent } from './components/login/login.component';
import { UserManagementComponent } from './components/user-management/user-management.component';
import { authGuard } from './guards/auth.guard';
import { AuthService } from './services/auth.service';

const adminGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if (authService.canManageUsers()) {
    return true;
  }
  router.navigate(['/dashboard']);
  return false;
};

const createGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if (authService.canCreate()) {
    return true;
  }
  router.navigate(['/inspections']);
  return false;
};

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'inspections', component: InspectionListComponent, canActivate: [authGuard] },
  { path: 'create', component: CreateInspectionComponent, canActivate: [authGuard, createGuard] },
  { path: 'sap-mock', component: SapMockComponent, canActivate: [authGuard] },
  { path: 'users', component: UserManagementComponent, canActivate: [authGuard, adminGuard] },
  { path: '**', redirectTo: 'dashboard' }
];
