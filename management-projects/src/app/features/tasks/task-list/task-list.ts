import { Component, input, output, OnInit, signal } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { TaskListResponse } from '../../../core/models/task';
import { TASK_STATUSES, getTaskSeverity, getTaskStatusLabel, Severity } from '../constants/task-constants';

@Component({
  selector: 'app-task-list',
  imports: [TableModule, TagModule, ButtonModule],
  templateUrl: './task-list.html',
})
export class TaskList {
  tasks = input<TaskListResponse[]>([]);
  loading = input(false);

  editTask = output<number>();
  deleteTask = output<number>();
  changeStatus = output<{ id: number; status: string }>();

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
