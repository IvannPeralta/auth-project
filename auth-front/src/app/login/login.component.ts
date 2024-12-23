import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html', 
  styleUrls: ['./login.component.css'],
  imports: [FormsModule],
})

export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    if (this.username && this.password) {
      this.authService.login(this.username, this.password).subscribe({
        next: (token) => {
          localStorage.setItem('token', token);
          this.router.navigate(['/home']);
        },
        error: (error) => {
          this.errorMessage = 'Usuario o contraseña incorrectos';
          console.log(error);
        }
      });
    } else {
      this.errorMessage = 'Ingrese nombre y usuario';
      console.log('Ingrese nombre y usuario');
    }
  }
}
