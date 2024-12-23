import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule], 
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  role: string = ''; 

  constructor (private authService: AuthService, private router: Router) {}

  hasRole(role: string): boolean {
    return this.authService.getUserRoles()?.includes(role) || false;
  }

  irView(role: string): void {
    if (role === 'admin') {
      this.router.navigate(['/view-admin']);
    } else {
      this.router.navigate(['/view-user']);
    }
  }
}
