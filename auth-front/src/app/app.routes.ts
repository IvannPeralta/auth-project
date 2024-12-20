import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ViewAdminComponent } from './view-admin/view-admin.component';
import { ViewUserComponent } from './view-user/view-user.component';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent },
    { path: 'view-admin', component: ViewAdminComponent },
    { path: 'view-user', component: ViewUserComponent }
  ];
