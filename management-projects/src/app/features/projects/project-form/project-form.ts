import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ProjectService } from '../../../core/services/project.service';
import { UserService } from '../../../core/services/user.service';
import { AuthService } from '../../../core/services/auth.service';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { User } from '../../../core/models/auth';
import { PROJECT_STATUSES } from '../constants/project-constants';
import { TaskSection } from '../../tasks/task-section/task-section';

@Component({
  selector: 'app-project-form',
  imports: [ReactiveFormsModule, CardModule, ButtonModule, ToastModule, SelectModule, DatePickerModule, InputTextModule, TextareaModule, TaskSection],
  templateUrl: './project-form.html',
  providers: [MessageService],
})
export class ProjectForm implements OnInit {
  private fb = inject(FormBuilder);
  private projectService = inject(ProjectService);
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private messageService = inject(MessageService);

  isEditMode = false;
  form: FormGroup;
  loading = false;
  users = signal<User[]>([]);
  projectStatus = signal<string | null>(null);
  private _projectId: number | null = null;

  get projectId(): number {
    return this._projectId!;
  }

  statusOptions = PROJECT_STATUSES;

  constructor() {
    const today = new Date();
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      status: ['active', Validators.required],
      startAt: [today, Validators.required],
      finishAt: ['', Validators.required],
      assignedId: ['', Validators.required],
    }, { validators: this.dateRangeValidator('startAt', 'finishAt') });
  }

  private dateRangeValidator(startCtrl: string, endCtrl: string) {
    return (group: FormGroup) => {
      const start = group.get(startCtrl)?.value;
      const end = group.get(endCtrl)?.value;
      if (!start || !end) return null;
      return new Date(start) < new Date(end) ? null : { dateRange: true };
    };
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.isEditMode = idParam !== null && idParam !== 'new';
    this._projectId = this.isEditMode ? Number(idParam) : null;

    this.userService.getAll().subscribe((users) => {
      this.users.set(users);
      if (this.isEditMode) {
        this.loadProject();
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

  private loadProject(): void {
    this.loading = true;
    this.projectService.getById(this._projectId!).subscribe({
      next: (res) => {
        const p = res.data;
        const matched = this.users().find((u) => u.fullName === p.assignedName);
        this.projectStatus.set(p.status ?? 'NONE');
        this.form.patchValue({
          name: p.name,
          description: p.description,
          status: p.status,
          startAt: new Date(p.startAt),
          finishAt: new Date(p.finishAt),
          assignedId: matched ? matched.id : null,
        });
        if (!this.authService.isAdmin()) {
          this.form.get('assignedId')?.disable();
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo cargar el proyecto' });
      },
    });
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
      id: this.isEditMode ? this._projectId : undefined,
      assignedName: selectedUser?.fullName ?? '',
      assignedId: selectedUser?.id ?? '',
    };
    console.log('Payload to submit:', payload);
    const request = this.isEditMode
      ? this.projectService.update(payload)
      : this.projectService.create(payload);

    request.subscribe({
      next: () => {
        const msg = this.isEditMode ? 'Proyecto actualizado exitosamente' : 'Proyecto creado exitosamente';
        this.messageService.add({ severity: 'success', summary: this.isEditMode ? 'Actualizado' : 'Creado', detail: msg });
        this.router.navigate(['/projects/home']);
      },
      error: (err) => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'No se pudo guardar el proyecto' });
      },
      complete: () => (this.loading = false),
    });
  }

  cancel(): void {
    this.router.navigate(['/projects/home']);
  }
}
