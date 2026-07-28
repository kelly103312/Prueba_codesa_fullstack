package com.codesa.user.service.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesa.user.service.project.dto.ApiResponse;
import com.codesa.user.service.project.dto.ChangeStatusRequest;
import com.codesa.user.service.project.dto.CreateProjectRequest;
import com.codesa.user.service.project.dto.ProjectDto;
import com.codesa.user.service.project.dto.ProjectListResponseDto;
import com.codesa.user.service.project.dto.UpdateProjectRequest;
import com.codesa.user.service.project.mapper.ProjectMapper;
import com.codesa.user.service.project.security.AuthenticatedUser;
import com.codesa.user.service.project.service.ProjectService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProjectDto>> createProject(
            @RequestBody CreateProjectRequest request,
            @AuthenticationPrincipal AuthenticatedUser user) {
        ProjectDto dto = projectMapper.toDtoFromCreate(request);
        dto.setOwnerId(user.getId());
        dto.setOwnerName(user.getName());
        ApiResponse<ProjectDto> response = projectService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ProjectDto>> updateProject(@RequestBody UpdateProjectRequest request) {
        ProjectDto dto = projectMapper.toDtoFromUpdate(request);
        ApiResponse<ProjectDto> response = projectService.update(dto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        ApiResponse<Void> response = projectService.delete(id);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<ProjectDto>> changeProjectStatus(@RequestBody ChangeStatusRequest request) {
        ApiResponse<ProjectDto> response = projectService.changeStatus(request.getId(), request.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/assigned")
    public ResponseEntity<ApiResponse<List<ProjectListResponseDto>>> getAssignedProjects(
            @AuthenticationPrincipal AuthenticatedUser user) {
        ApiResponse<List<ProjectListResponseDto>> response = projectService.getProjectsByAssignedId(user.getId(), user.getRole());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDto>> getProject(@PathVariable Long id) {
        ApiResponse<ProjectDto> response = projectService.getById(id);
        return ResponseEntity.ok(response);
    }

}
