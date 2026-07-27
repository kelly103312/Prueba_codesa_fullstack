import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, PaginatedResponse } from '../models/api-response';
import { Project, ProjectListResponse, ProjectStatusRequest } from '../models/project';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private readonly baseUrl = environment.apiUrlProjects;

  constructor(private http: HttpClient) {}

  getAll(params?: Record<string, string | number | boolean | undefined>): Observable<PaginatedResponse<Project>> {
    let httpParams = new HttpParams();
    if (params) {
      for (const [key, value] of Object.entries(params)) {
        if (value !== undefined && value !== null) {
          httpParams = httpParams.set(key, String(value));
        }
      }
    }
    return this.http.get<PaginatedResponse<Project>>(`${this.baseUrl}/project`, { params: httpParams });
  }

  getAssigned(): Observable<ApiResponse<ProjectListResponse[]>> {
    return this.http.get<ApiResponse<ProjectListResponse[]>>(`${this.baseUrl}/project/assigned`);
  }

  getById(id: number): Observable<ApiResponse<Project>> {
    return this.http.get<ApiResponse<Project>>(`${this.baseUrl}/project/${id}`);
  }

  create(project: Project): Observable<ApiResponse<Project>> {
    return this.http.post<ApiResponse<Project>>(`${this.baseUrl}/project/create`, project);
  }

  update(project:Project): Observable<ApiResponse<Project>> {
    return this.http.put<ApiResponse<Project>>(`${this.baseUrl}/project/update`, project);
  }

  changeStatusByID(id:number, status: string): Observable<ApiResponse<Project>> {
    const statusProject = { id, status };
    return this.http.patch<ApiResponse<Project>>(`${this.baseUrl}/project/status`, statusProject);
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/project/${id}`);
  }
}
