export interface PaginationConfig {
  page: number;
  pageSize: number;
  sortField?: string;
  sortOrder?: 'asc' | 'desc';
}

export interface Pageable {
  page: number;
  size: number;
  sort?: string;
}
