import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

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
}

