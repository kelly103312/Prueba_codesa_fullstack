import { Component, inject, input, output, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectModule } from 'primeng/select';
import { TaskService } from '../../../core/services/task.service';
import { UserService } from '../../../core/services/user.service';
import { User } from '../../../core/models/auth';
import { Task } from '../../../core/models/task';
import { TASK_STATUSES } from '../constants/task-constants';

@Component({
  selector: 'app-task-form',
  imports: [ReactiveFormsModule, ButtonModule, InputTextModule, TextareaModule, DatePickerModule, SelectModule],
  templateUrl: './task-form.html',
})
export class TaskForm implements OnInit {
  private fb = inject(FormBuilder);
  private taskService = inject(TaskService);
  private userService = inject(UserService);

  projectId = input.required<number>();
  task = input<Task | null>(null);

  saved = output<void>();
  cancelled = output<void>();

  isEditMode = false;
  form: FormGroup;
  loading = false;
  users = signal<User[]>([]);

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

    const taskData = this.task();
    if (taskData) {
      this.isEditMode = true;
      this.form.patchValue({
        ...taskData,
        startAt: new Date(taskData.startAt),
        finishAt: new Date(taskData.finishAt),
        dueDate: new Date(taskData.dueDate),
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const formValue = this.form.value;
    const selectedUser = this.users().find((u) => u.id === formValue.assignedId);

    const payload = {
      ...formValue,
      projectId: this.projectId(),
      assignedName: selectedUser?.fullName ?? '',
    };

    const request = this.isEditMode
      ? this.taskService.update({ ...payload, id: this.task()!.id })
      : this.taskService.create(payload);

    request.subscribe({
      next: () => {
        this.saved.emit();
      },
      error: () => {
        this.loading = false;
      },
      complete: () => (this.loading = false),
    });
  }

  cancel(): void {
    this.cancelled.emit();
  }
}
