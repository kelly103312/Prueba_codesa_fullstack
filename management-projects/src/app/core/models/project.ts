import { Task } from "./task";

export interface Project {
  id: number;
  name: string;
  description: string;
  status?: string;
  assignedId: string;
  assignedName: string;
  ownerId: string;
  ownerName: string;
  startAt: Date;
  finishAt: Date;
  task?: Task[];
}

export interface ProjectStatusRequest{
  id:number;
  status: string;
}

export interface ProjectListResponse {
  id: number;
  name: string;
  description: string;
  status: string;
  startAt: Date;
  finishAt: Date;
  ownerId: string;
  ownerName: string;
  assignedId: string;
  assignedName: string;
}
