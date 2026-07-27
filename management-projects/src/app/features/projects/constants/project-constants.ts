export const PROJECT_STATUSES = [
  { label: 'Activo', value: 'ACTIVE' },
  { label: 'Archivado', value: 'ARCHIVED' },
  { label: 'Cerrado', value: 'CLOSED' },
];

export function getProjectStatusLabel(value: string): string {
  return PROJECT_STATUSES.find((s) => s.value === value)?.label ?? value;
}

export type Severity = 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast';

export function getProjectSeverity(status: string): Severity {
  switch (status) {
    case 'ACTIVE':
      return 'success';
    case 'ARCHIVED':
      return 'contrast';
    case 'CLOSED':
      return 'secondary';
    default:
      return 'info';
  }
}
