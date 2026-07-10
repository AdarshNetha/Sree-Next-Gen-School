import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home.component';
import { AboutUsComponent } from './pages/about-us.component';
import { AcademicsComponent } from './pages/academics.component';
import { FacilitiesComponent } from './pages/facilities.component';
import { ContactComponent } from './pages/contact.component';
import { RequestAdmissionComponent } from './pages/request-admission.component';
import { AdminAccessComponent } from './pages/admin-access.component';
import { AdminDashboardComponent } from './pages/admin-dashboard.component';
import { adminAuthGuard } from './core/admin-auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'about-us', component: AboutUsComponent },
  { path: 'academics', component: AcademicsComponent },
  { path: 'facilities', component: FacilitiesComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'request-admission', component: RequestAdmissionComponent },
  { path: 'admin/login', component: AdminAccessComponent },
  {
    path: 'admin',
    component: AdminDashboardComponent,
    canActivate: [adminAuthGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'requests' },
      {
        path: 'requests',
        loadComponent: () => import('./pages/admin-requests.component').then((m) => m.AdminRequestsComponent),
      },
    ],
  },
  { path: 'admin/requests', redirectTo: 'admin', pathMatch: 'full' },
  { path: '**', redirectTo: '' },
];
