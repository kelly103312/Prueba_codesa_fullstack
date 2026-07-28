import { Component, input, output, signal } from '@angular/core';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { TaskListResponse } from '../../../core/models/task';
import { TASK_STATUSES, getTaskSeverity, getTaskStatusLabel, Severity } from '../constants/task-constants';

@Component({
  selector: 'app-task-list',
  imports: [IconFieldModule, InputIconModule, InputTextModule, TableModule, TagModule, ButtonModule, SelectModule, FormsModule],
  templateUrl: './task-list.html',
})
export class TaskList {
  tasks = input<TaskListResponse[]>([]);
  loading = input(false);
  showAddButton = input(false);
  searchValue = '';

  filterOptions = TASK_STATUSES;

  editTask = output<number>();
  deleteTask = output<number>();
  addTask = output<void>();

  getSeverity(status: string): Severity { return getTaskSeverity(status); }
  getStatusLabel(value: string) { return getTaskStatusLabel(value); }
  formatDate(date: Date) {
    if (!date) return '';
    const fecha = new Date(date);
    const month = String(fecha.getMonth() + 1).padStart(2, '0');
    const day = String(fecha.getDate()).padStart(2, '0');
    const year = fecha.getFullYear();
    return `${month}/${day}/${year}`;
  }
}
