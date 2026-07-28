export interface Task {
  id: number;
  projectId: number;
  name: string;
  description: string;
  status: string;
  assignedId: string; 
  assignedName: string;
  startAt: string; 
  finishAt: string;
  dueDate: string;
}

export interface TaskStatusRequest{
  id:number;
  status: string;
}

export interface TaskListResponse {
  id: number;
  projectId: number;
  name: string;
  description: string;
  status: string;
  assignedId: string; 
  assignedName: string;
  startAt: string; 
  finishAt: string;
  dueDate: string;
}
