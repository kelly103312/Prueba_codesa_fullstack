import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response';
import { RegisterRequest, User } from '../models/auth';
import { HashService } from './hash.service';
import { from, map, Observable, switchMap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly baseUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient,private hashService: HashService) {}

  getAll(): Observable<User[]> {
    return this.http.get<ApiResponse<User[]>>(`${this.baseUrl}/all`).pipe(map(res => res.data));
  }

  getById(id: string): Observable<User> {
    return this.http.get<ApiResponse<User>>(`${this.baseUrl}/${id}`).pipe(map(res => res.data));
  }
  

  register(data: RegisterRequest): Observable<ApiResponse<User>> {
    return from(this.hashService.sha256(data.password)).pipe(
      switchMap((hashedPassword) => {
        const request: RegisterRequest = {
          ...data,
          password: hashedPassword,
        };

        return this.http.post<ApiResponse<User>>(
          `${this.baseUrl}/create`,
          request
        );
      })
    );
  }
}
