import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  constructor (private authService: AuthService, private router: Router) {}

  irHome() {
    this.router.navigate(['/home']);
  }

  logout() {
    this.authService.logout();
  }
}