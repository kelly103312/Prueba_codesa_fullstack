import { Component, OnInit, signal } from '@angular/core';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule, Table } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ProjectListResponse } from '../../../core/models/project';
import { ProjectService } from '../../../core/services/project.service';
import { PROJECT_STATUSES, getProjectSeverity, getProjectStatusLabel, Severity } from '../constants/project-constants';

@Component({
  selector: 'app-project-list',
  imports: [SelectModule, IconFieldModule, InputIconModule, TableModule, TagModule, ButtonModule, InputTextModule, FormsModule, ToastModule],
  templateUrl: './project-list.html',
  styleUrl: './project-list.scss',
  providers: [MessageService],
})
export class ProjectList implements OnInit {
    projects = signal<ProjectListResponse[]>([]);
    statuses = signal<any[]>([]);
    loading = signal(true);
    searchValue: string = '';

    constructor(
        private projectService: ProjectService,
        private router: Router,
        private confirmationService: ConfirmationService,
        private messageService: MessageService,
    ) {}

    ngOnInit() {
        this.statuses.set(PROJECT_STATUSES);
        this.loadProjects();
    }

    private loadProjects(): void {
        this.loading.set(true);
        this.projectService.getAssigned().subscribe({
            next: (res) => {
                this.projects.set(res.data);
                this.loading.set(false);
            },
            error: () => {
                this.loading.set(false);
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudieron cargar los proyectos' });
            },
        });
    }

    viewProject(project: ProjectListResponse) {
        this.router.navigate(['/projects', project.id, 'edit']);
    }

    archiveProject(id: number): void {
        this.confirmationService.confirm({
            message: '¿Estás seguro de que deseas archivar este proyecto?',
            header: 'Archivar proyecto',
            icon: 'pi pi-box',
            acceptLabel: 'Sí, archivar',
            rejectLabel: 'Cancelar',
            acceptButtonStyleClass: 'p-button-success',
            rejectButtonStyleClass: 'p-button-secondary',
            accept: () => {
                this.projectService.changeStatusByID(id, 'ARCHIVED').subscribe({
                    next: () => {
                        this.messageService.add({ severity: 'success', summary: 'Archivado', detail: 'Proyecto archivado exitosamente' });
                        this.loadProjects();
                    },
                    error: (err) => {
                        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'No se pudo archivar el proyecto' });
                    },
                });
            },
        });
    }

    deleteProject(id: number) {
        this.confirmationService.confirm({
            message: '¿Estás seguro de que deseas eliminar este proyecto?',
            header: 'Eliminar proyecto',
            icon: 'pi pi-trash',
            acceptLabel: 'Sí, eliminar',
            rejectLabel: 'Cancelar',
            acceptButtonStyleClass: 'p-button-primary',
            rejectButtonStyleClass: 'p-button-secondary',
            accept: () => {
                this.projectService.delete(id).subscribe({
                    next: () => {
                        this.messageService.add({ severity: 'success', summary: 'Eliminado', detail: 'Proyecto eliminado exitosamente' });
                        this.loadProjects();
                    },
                    error: (err) => {
                        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'No se pudo eliminar el proyecto' });
                    },
                });
            },
        });
    }

    clear(table: Table) {
        table.clear();
        this.searchValue = '';
    }

    add(){
        this.router.navigate(['/projects/new']);
    }

    formatDate(date: Date) {
        if (!date) return '';
        const fecha = new Date(date);
        const month = String(fecha.getMonth() + 1).padStart(2, '0');
        const day = String(fecha.getDate()).padStart(2, '0');
        const year = fecha.getFullYear();
        return `${month}/${day}/${year}`;
    }

    getSeverity(status: string): Severity { return getProjectSeverity(status); }
    getStatusLabel(value: string) { return getProjectStatusLabel(value); }
}
