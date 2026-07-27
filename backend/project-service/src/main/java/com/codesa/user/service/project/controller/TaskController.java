package com.codesa.user.service.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesa.user.service.project.dto.ApiResponse;
import com.codesa.user.service.project.dto.ChangeStatusRequest;
import com.codesa.user.service.project.dto.CreateTaskRequest;
import com.codesa.user.service.project.dto.TaskDto;
import com.codesa.user.service.project.dto.TaskListResponseDto;
import com.codesa.user.service.project.dto.UpdateTaskRequest;
import com.codesa.user.service.project.mapper.TaskMapper;
import com.codesa.user.service.project.service.TaskService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping("/{projectId}/create")
    public ResponseEntity<ApiResponse<TaskDto>> createTask(
            @PathVariable Long projectId,
            @RequestBody CreateTaskRequest request) {
        TaskDto dto = taskMapper.toDtoFromCreate(request);
        ApiResponse<TaskDto> response = taskService.create(projectId, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<TaskDto>> updateTask(@RequestBody UpdateTaskRequest request) {
        TaskDto dto = taskMapper.toDtoFromUpdate(request);
        ApiResponse<TaskDto> response = taskService.update(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskListResponseDto>>> getTasksByProject(@PathVariable Long projectId) {
        ApiResponse<List<TaskListResponseDto>> tasks = taskService.getByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        ApiResponse<Void> response = taskService.delete(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<TaskDto>> changeTaskStatus(@RequestBody ChangeStatusRequest request) {
        ApiResponse<TaskDto> response = taskService.changeStatus(request.getId(), request.getStatus());
        return ResponseEntity.ok(response);
    }
}
