import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode'; 

@Injectable({
    providedIn: 'root'
})

export class AuthService {

    private apiUrl = 'http://localhost:8080/api/auth/login';

    constructor(private http: HttpClient, private router: Router) { }

    login(username: string, password: string): Observable<string> {
        return this.http.post(this.apiUrl, { username, password }, { responseType: 'text' });
    }

    logout() {
        localStorage.removeItem('token');
        this.router.navigate(['/login']);
    }     
    
    /* Método para obtener el token del almacenamiento local */
    getToken(): string | null {
        return localStorage.getItem('token'); 
      }
    
      /* Método para decodificar el token JWT */
      getDecodedToken(): any {
        const token = this.getToken();
        return token ? jwtDecode(token) : null; 
      }

    getUserRoles(): string[] | null {
        const decodedToken = this.getDecodedToken();
        return decodedToken && decodedToken.roles ? decodedToken.roles : null; // Ajusta 'roles' si tu payload usa un campo diferente.
      }
}

