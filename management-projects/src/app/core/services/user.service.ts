import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { User } from '../models/auth';

@Injectable({ providedIn: 'root' })
export class UserService {
  private mockUsers: User[] = [
    { id: 'dc492856-5710-4a33-a069-e3ae0d1813bc', email: 'admin@codesa.com', name: 'Admin Codesa', roles: ['ADMIN'] },
    { id: 'dc492856-5710-4a33-a069-e3ae0d1813bd', email: 'carlos@codesa.com', name: 'Carlos Mendoza', roles: ['USER'] }
  ];

  getAll(): Observable<User[]> {
    return of(this.mockUsers);
  }

  getById(id: string): Observable<User | undefined> {
    return of(this.mockUsers.find((u) => u.id === id));
  }
}
