package com.codesa.user.service.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    @Schema(description = "Título de la tarea", example = "Implementar login")
    private String name;
    @Schema(description = "ID del proyecto")
    private Long projectId;
    @Schema(description = "Descripción de la tarea")
    private String description;
    @Schema(description = "Estado de la tarea (PENDING, INPROGRESS, DONE, OVERDUE)")
    private String status;
    @Schema(description = "ID del usuario asignado")
    private UUID assignedId;
    @Schema(description = "Nombre del usuario asignado")
    private String assignedName;
    @Schema(description = "Fecha de inicio de la tare")
    private LocalDateTime startAt;
    @Schema(description = "Fecha de finalización de la tarea")
    private LocalDateTime finishAt;
    @Schema(description = "Fecha de vencimiento de la tarea")
    private LocalDateTime dueDate;
}
