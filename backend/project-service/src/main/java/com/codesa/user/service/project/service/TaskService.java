package com.codesa.user.service.project.service;

import com.codesa.user.service.project.dto.ApiResponse;
import com.codesa.user.service.project.dto.TaskDto;
import com.codesa.user.service.project.entity.ProjectEntity;
import com.codesa.user.service.project.entity.TaskEntity;
import com.codesa.user.service.project.exception.ConflictException;
import com.codesa.user.service.project.exception.NotFoundException;
import com.codesa.user.service.project.mapper.TaskMapper;
import com.codesa.user.service.project.repository.ProjectRepository;
import com.codesa.user.service.project.repository.TaskRepository;

import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
    }

    public ApiResponse<TaskDto> create(TaskDto dto) {
        try {
            if (taskRepository.existsByNameAndProjectId(dto.getName(),dto.getProjectId())) {
                throw new ConflictException("Task already exists in project with name: " + dto.getName());
            }
            
            ProjectEntity project = projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new NotFoundException("Project not found"));
            TaskEntity task = taskMapper.toEntity(dto);
            task.setProject(project);
            task = taskRepository.save(task);
            return ApiResponse.created("Task created",taskMapper.toDto(task));

        } catch (ConflictException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error creating task: " + e.getMessage());
        }
    }
}
