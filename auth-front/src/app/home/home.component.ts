import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule], 
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  role: string = 'user'; // Simula el rol del usuario

  hasPermission(role: string): boolean {
    // Simula permisos basados en el rol
    return this.role === role;
  }
}
