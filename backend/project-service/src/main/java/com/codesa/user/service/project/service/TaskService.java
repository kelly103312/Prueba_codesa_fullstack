package com.codesa.user.service.project.service;

import com.codesa.user.service.project.dto.ApiResponse;
import com.codesa.user.service.project.dto.TaskDto;
import com.codesa.user.service.project.dto.TaskListResponseDto;
import com.codesa.user.service.project.entity.ProjectEntity;
import com.codesa.user.service.project.entity.TaskEntity;
import com.codesa.user.service.project.enums.TaskStatus;
import com.codesa.user.service.project.exception.InvalidStatusException;
import com.codesa.user.service.project.exception.NotFoundException;
import com.codesa.user.service.project.mapper.TaskMapper;
import com.codesa.user.service.project.repository.ProjectRepository;
import com.codesa.user.service.project.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
    }

    public ApiResponse<TaskDto> create(Long projectId, TaskDto dto) {
        try {
            ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found with id: " + projectId));

            TaskEntity entity = taskMapper.toEntity(dto);
            entity.setProject(project);
            entity = taskRepository.save(entity);

            return ApiResponse.created("Task created", taskMapper.toDto(entity));

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error creating task: " + e.getMessage());
        }
    }

    public ApiResponse<TaskDto> getById(Long id) {
        TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        return ApiResponse.ok("OK", taskMapper.toDto(entity));
    }

    public ApiResponse<List<TaskListResponseDto>> getByProjectId(Long projectId) {
        List<TaskListResponseDto> tasks = taskMapper.toListDto(taskRepository.findByProjectId(projectId));
        return ApiResponse.ok("OK", tasks);
    }

    public ApiResponse<TaskDto> update(TaskDto dto) {
        try {
            TaskEntity entity = taskRepository.findById(dto.getId()).orElseThrow(() -> new NotFoundException("Task not found with id: " + dto.getId()));

            taskMapper.updateEntity(dto, entity);
            entity = taskRepository.save(entity);

            return ApiResponse.ok("Task updated", taskMapper.toDto(entity));

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error updating task: " + e.getMessage());
        }
    }

    public ApiResponse<Void> delete(Long id) {
        try {
            TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));

            taskRepository.delete(entity);
            return ApiResponse.ok("Task deleted", null);

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error deleting task: " + e.getMessage());
        }
    }

    public ApiResponse<TaskDto> changeStatus(Long id, String status) {
        try {
            if (!TaskStatus.isValid(status)) {
                throw new InvalidStatusException("Invalid status: " + status + ". Allowed statuses: " + java.util.Arrays.toString(TaskStatus.values()));
            }

            TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));

            entity.setStatus(status.toUpperCase());
            entity = taskRepository.save(entity);

            return ApiResponse.ok("Task status changed", taskMapper.toDto(entity));

        } catch (NotFoundException | InvalidStatusException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error changing task status: " + e.getMessage());
        }
    }
}
