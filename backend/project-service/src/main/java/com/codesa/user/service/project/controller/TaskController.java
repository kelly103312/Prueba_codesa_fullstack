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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/task")
@Tag(name = "Tareas", description = "Gestión de tareas CRUD")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping("/{projectId}/create")
    @Operation(summary = "Crear tarea", description = "Crea una nueva tarea dentro de un proyecto")
    @Parameter(description = "ID numérico del proyecto")
    public ResponseEntity<ApiResponse<TaskDto>> createTask(
            @PathVariable Long projectId,
            @RequestBody CreateTaskRequest request) {
        TaskDto dto = taskMapper.toDtoFromCreate(request);
        ApiResponse<TaskDto> response = taskService.create(projectId, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @Operation(summary = "Actualizar tarea", description = "Actualiza los datos de una tarea existente")
    public ResponseEntity<ApiResponse<TaskDto>> updateTask(@RequestBody UpdateTaskRequest request) {
        TaskDto dto = taskMapper.toDtoFromUpdate(request);
        ApiResponse<TaskDto> response = taskService.update(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Tareas por proyecto", description = "Lista todas las tareas de un proyecto")
    @Parameter(description = "ID numérico del proyecto")
    public ResponseEntity<ApiResponse<List<TaskListResponseDto>>> getTasksByProject(@PathVariable Long projectId) {
        ApiResponse<List<TaskListResponseDto>> tasks = taskService.getByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarea por ID", description = "Busca una tarea por su ID numérico")
    @Parameter(description = "ID numérico de la tarea")
    public ResponseEntity<ApiResponse<TaskDto>> getTasksByID(@PathVariable Long id) {
        ApiResponse<TaskDto> tasks = taskService.getById(id);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tarea", description = "Elimina una tarea por su ID")
    @Parameter(description = "ID numérico de la tarea")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        ApiResponse<Void> response = taskService.delete(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/status")
    @Operation(summary = "Cambiar estado de tarea", description = "Cambia el estado de una tarea (PENDING/INPROGRESS/DONE/OVERDUE)")
    public ResponseEntity<ApiResponse<TaskDto>> changeTaskStatus(@RequestBody ChangeStatusRequest request) {
        ApiResponse<TaskDto> response = taskService.changeStatus(request.getId(), request.getStatus());
        return ResponseEntity.ok(response);
    }
}
