import { Component, inject, input, OnInit, signal } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TaskService } from '../../../core/services/task.service';
import { Task, TaskListResponse } from '../../../core/models/task';
import { TaskList } from '../task-list/task-list';
import { TaskForm } from '../task-form/task-form';

@Component({
  selector: 'app-task-section',
  imports: [CardModule, ButtonModule, DialogModule, ToastModule, TaskList, TaskForm],
  templateUrl: './task-section.html',
  providers: [MessageService],
})
export class TaskSection implements OnInit {
  private taskService = inject(TaskService);
  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);

  projectId = input.required<number>();
  archived = input(false);

  tasks = signal<TaskListResponse[]>([]);
  loading = signal(false);
  showDialog = signal(false);
  editingTask = signal<Task | null>(null);

  ngOnInit(): void {
    console.log(this.archived)
    this.loadTasks();
  }

  private loadTasks(): void {
    this.loading.set(true);
    this.taskService.getAllByProject(this.projectId()).subscribe({
      next: (res) => {
        this.tasks.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudieron cargar las tareas' });
      },
    });
  }

  openNew(): void {
    this.editingTask.set(null);
    this.showDialog.set(true);
  }

  openEdit(taskId: number): void {
    this.taskService.getById(taskId).subscribe({
      next: (res) => {
        this.editingTask.set(res.data);
        this.showDialog.set(true);
      },
      error: (err) => this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'No se pudo cargar la tarea' }),
    });
  }

  onSaved(): void {
    this.showDialog.set(false);
    this.editingTask.set(null);
    this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Tarea guardada exitosamente' });
    this.loadTasks();
  }

  onCancelled(): void {
    this.showDialog.set(false);
    this.editingTask.set(null);
  }

  onDelete(taskId: number): void {
    this.confirmationService.confirm({
      message: '¿Estás seguro de que deseas eliminar esta tarea?',
      header: 'Eliminar tarea',
      icon: 'pi pi-trash',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-primary',
      rejectButtonStyleClass: 'p-button-secondary',
      accept: () => {
        this.taskService.delete(taskId).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Eliminada', detail: 'Tarea eliminada exitosamente' });
            this.loadTasks();
          },
          error: (err) => this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'No se pudo eliminar la tarea' }),
        });
      },
    });
  }
}
