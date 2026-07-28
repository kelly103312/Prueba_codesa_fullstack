package com.codesa.user.service.project.service;

import com.codesa.user.service.project.dto.ApiResponse;
import com.codesa.user.service.project.dto.ProjectDto;
import com.codesa.user.service.project.dto.ProjectListResponseDto;
import com.codesa.user.service.project.entity.ProjectEntity;
import com.codesa.user.service.project.enums.ProjectStatus;
import com.codesa.user.service.project.enums.Role;
import com.codesa.user.service.project.exception.ConflictException;
import com.codesa.user.service.project.exception.InvalidStatusException;
import com.codesa.user.service.project.exception.NotFoundException;
import com.codesa.user.service.project.mapper.ProjectMapper;
import com.codesa.user.service.project.repository.ProjectRepository;
import com.codesa.user.service.project.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.projectMapper = projectMapper;
    }

    public ApiResponse<ProjectDto> create(ProjectDto dto) {
        try {
            if (projectRepository.existsByName(dto.getName())) {
                throw new ConflictException("Project already exists with name: " + dto.getName());
            }
            ProjectEntity entity = projectMapper.toEntity(dto);
            entity = projectRepository.save(entity);
            return ApiResponse.created("Project created", projectMapper.toDto(entity));

        } catch (ConflictException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error creating project: " + e.getMessage());
        }
    }

    public ApiResponse<ProjectDto> getById(Long id) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found with id: " + id));
        return ApiResponse.ok("OK", projectMapper.toDto(entity));
    }

    public ApiResponse<List<ProjectListResponseDto>> getAll() {
        List<ProjectListResponseDto> projects = projectMapper.toListDto(projectRepository.findAll());
        return ApiResponse.ok("OK", projects);
    }

    public ApiResponse<ProjectDto> update(ProjectDto dto) {
        try {
            ProjectEntity entity = projectRepository.findById(dto.getId())
                    .orElseThrow(() -> new NotFoundException("Project not found with id: " + dto.getId()));

            projectMapper.updateEntity(dto, entity);
            entity = projectRepository.save(entity);
            return ApiResponse.ok("Project updated", projectMapper.toDto(entity));

        } catch (NotFoundException | ConflictException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error updating project: " + e.getMessage());
        }
    }

    public ApiResponse<Void> delete(Long id) {
        try {
            ProjectEntity entity = projectRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Project not found with id: " + id));

            if (taskRepository.existsByProjectId(id)) {
                return ApiResponse.error(400, "Cannot delete project with existing tasks");
            }
            projectRepository.delete(entity);
            return ApiResponse.ok("Project deleted", null);

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error deleting project: " + e.getMessage());
        }
    }

    public ApiResponse<List<ProjectListResponseDto>> getProjectsByAssignedId(UUID assignedId, Role role) {
        try {
            List<ProjectListResponseDto> projects;

            if(Role.ADMIN.equals(role)){
                projects = projectMapper.toListDto(projectRepository.findAll());
            }else{
                projects = projectMapper.toListDto(projectRepository.findByAssignedId(assignedId));
            }
            return ApiResponse.ok("OK", projects);

        } catch (Exception e) {
            return ApiResponse.error(500, "Error fetching assigned projects: " + e.getMessage());
        }
    }

    public ApiResponse<ProjectDto> changeStatus(Long id, String status) {
        try {
            if (!ProjectStatus.isValid(status)) {
                throw new InvalidStatusException("Invalid status: " + status + ". Allowed statuses: " + java.util.Arrays.toString(ProjectStatus.values()));
            }

            ProjectEntity entity = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project not found with id: " + id));

            entity.setStatus(status.toUpperCase());
            entity = projectRepository.save(entity);

            return ApiResponse.ok("Project status changed", projectMapper.toDto(entity));

        } catch (NotFoundException | InvalidStatusException e) {
            throw e;
        } catch (Exception e) {
            return ApiResponse.error(500, "Error changing project status: " + e.getMessage());
        }
    }
}
