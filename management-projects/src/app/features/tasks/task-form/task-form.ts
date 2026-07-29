import { Component, inject, input, output, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectModule } from 'primeng/select';
import { TaskService } from '../../../core/services/task.service';
import { UserService } from '../../../core/services/user.service';
import { AuthService } from '../../../core/services/auth.service';
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
  private authService = inject(AuthService);

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
    }, { validators: this.taskDateValidator });
  }

  private taskDateValidator(group: FormGroup) {
    const start = group.get('startAt')?.value;
    const finish = group.get('finishAt')?.value;
    const due = group.get('dueDate')?.value;

    if (!start || !finish || !due) return null;
    const errors: Record<string, boolean> = {};

    if (new Date(start) >= new Date(finish)) {
      errors['dateRange'] = true;
    }
    if (new Date(start) > new Date(due)) {
      errors['dueBeforeStart'] = true;
    }

    return Object.keys(errors).length ? errors : null;
  }

  ngOnInit(): void {
    this.userService.getAll().subscribe((users) => {
      this.users.set(users);

      const taskData = this.task();
      if (taskData) {
        this.isEditMode = true;
        this.form.patchValue({
          ...taskData,
          startAt: new Date(taskData.startAt),
          finishAt: new Date(taskData.finishAt),
          dueDate: new Date(taskData.dueDate),
        });
        if (!this.authService.isAdmin()) {
          this.form.get('assignedId')?.disable();
        }
      } else {
        this.assignToCurrentUser();
      }
    });
  }

  private assignToCurrentUser(): void {
    const currentUser = this.authService.user();
    if (!currentUser) return;
    this.form.patchValue({ assignedId: currentUser.id });
    if (!this.authService.isAdmin()) {
      this.form.get('assignedId')?.disable();
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const formValue = this.form.getRawValue();
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
