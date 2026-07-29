import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, PaginatedResponse } from '../models/api-response';
import { Task, TaskListResponse } from '../models/task';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private readonly baseUrl = environment.apiUrlProjects;

  constructor(private http: HttpClient) {}

  getAllByProject(projectId: number, params?: Record<string, string | number | boolean | undefined>): Observable<PaginatedResponse<Task>> {
    let httpParams = new HttpParams();
    if (params) {
      for (const [key, value] of Object.entries(params)) {
        if (value !== undefined && value !== null) {
          httpParams = httpParams.set(key, String(value));
        }
      }
    }
    return this.http.get<PaginatedResponse<Task>>(`${this.baseUrl}/task/project/${projectId}`, { params: httpParams });
  }

  getById(id: number): Observable<ApiResponse<Task>> {
    return this.http.get<ApiResponse<Task>>(`${this.baseUrl}/task/${id}`);
  }

  create(task: Task): Observable<ApiResponse<Task>> {
    const project_id = task.projectId.toString();
    return this.http.post<ApiResponse<Task>>(`${this.baseUrl}/task/${project_id}/create`, task);
  }

  update(task: Task): Observable<ApiResponse<Task>> {
    console.log('task:', task);
    return this.http.put<ApiResponse<Task>>(`${this.baseUrl}/task/update`, task);
  }

  changeStatusByID(id: number, status: string): Observable<ApiResponse<Task>> {
    return this.http.patch<ApiResponse<Task>>(`${this.baseUrl}/task/status`, { id, status });
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/task/${id}`);
  }
}
