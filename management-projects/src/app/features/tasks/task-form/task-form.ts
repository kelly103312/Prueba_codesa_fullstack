import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectModule } from 'primeng/select';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { TaskService } from '../../../core/services/task.service';
import { UserService } from '../../../core/services/user.service';
import { User } from '../../../core/models/auth';
import { TASK_STATUSES } from '../constants/task-constants';

@Component({
  selector: 'app-task-form',
  imports: [ReactiveFormsModule, CardModule, ButtonModule, InputTextModule, TextareaModule, DatePickerModule, SelectModule, ToastModule],
  templateUrl: './task-form.html',
  providers: [MessageService],
})
export class TaskForm implements OnInit {
  private fb = inject(FormBuilder);
  private taskService = inject(TaskService);
  private userService = inject(UserService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private messageService = inject(MessageService);

  isEditMode = false;
  form: FormGroup;
  loading = false;
  users = signal<User[]>([]);
  private taskId: number | null = null;
  private projectId: number | null = null;

  statusOptions = TASK_STATUSES;

  constructor() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      status: ['PENDING', Validators.required],
      assignedId: ['', Validators.required],
      startAt: ['', Validators.required],
      finishAt: ['', Validators.required],
      dueDate: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.userService.getAll().subscribe((users) => this.users.set(users));

    const taskId = this.route.snapshot.paramMap.get('taskId');
    const projectId = this.route.snapshot.paramMap.get('projectId');
    this.projectId = projectId ? Number(projectId) : null;

    if (taskId && taskId !== 'new') {
      this.taskId = Number(taskId);
      this.isEditMode = true;
      this.loadTask();
    }
  }

  private loadTask(): void {
    this.loading = true;
    this.taskService.getById(this.taskId!).subscribe({
      next: (res) => {
        const t = res.data;
        const matched = this.users().find((u) => u.id === t.assignedId);
        this.form.patchValue({
          name: t.name,
          description: t.description,
          status: t.status,
          assignedId: matched ? matched.id : t.assignedId,
          startAt: new Date(t.startAt),
          finishAt: new Date(t.finishAt),
          dueDate: new Date(t.dueDate),
        });
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo cargar la tarea' });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const formValue = this.form.value;

    const payload = {
      ...formValue,
      projectId: this.projectId,
    };

    const request = this.isEditMode
      ? this.taskService.update({ ...payload, id: this.taskId! })
      : this.taskService.create(payload);

    request.subscribe({
      next: () => {
        const msg = this.isEditMode ? 'Tarea actualizada exitosamente' : 'Tarea creada exitosamente';
        this.messageService.add({ severity: 'success', summary: this.isEditMode ? 'Actualizado' : 'Creado', detail: msg });
        setTimeout(() => this.router.navigate(['/projects', this.projectId, 'detail']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'No se pudo guardar la tarea' });
      },
      complete: () => (this.loading = false),
    });
  }

  cancel(): void {
    this.router.navigate(['/projects', this.projectId, 'detail']);
  }
}
