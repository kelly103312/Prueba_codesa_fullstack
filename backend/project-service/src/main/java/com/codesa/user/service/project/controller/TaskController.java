package com.codesa.user.service.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesa.user.service.project.dto.ApiResponse;
import com.codesa.user.service.project.dto.CreateTaskRequest;
import com.codesa.user.service.project.dto.TaskDto;
import com.codesa.user.service.project.mapper.TaskMapper;
import com.codesa.user.service.project.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskDto>> createTask(@RequestBody CreateTaskRequest request) {
        TaskDto dto = taskMapper.toDtoFromCreate(request);
        ApiResponse<TaskDto> response = taskService.create(dto);
        return ResponseEntity.ok(response);
    }

}
