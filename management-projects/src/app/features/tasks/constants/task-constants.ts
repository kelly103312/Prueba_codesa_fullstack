export const TASK_STATUSES = [
  { label: 'Creada', value: 'CREATED' },
  { label: 'En Progreso', value: 'IN_PROGRESS' },
  { label: 'Completado', value: 'DONE' },
  { label: 'Vencida', value: 'OVERDUE' },
];

export function getTaskStatusLabel(value: string): string {
  return TASK_STATUSES.find((s) => s.value === value)?.label ?? value;
}

export type Severity = 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast';

export function getTaskSeverity(status: string): Severity {
  switch (status) {
    case 'CREATED':
      return 'info';
    case 'IN_PROGRESS':
      return 'warn';
    case 'DONE':
      return 'success';
    case 'OVERDUE':
      return 'danger';
    default:
      return 'info';
  }
}
