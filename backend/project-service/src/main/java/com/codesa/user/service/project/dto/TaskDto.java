package com.codesa.user.service.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class TaskDto {

    @Schema(description = "ID de la tarea", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del proyecto al que pertenece")
    private Long projectId;

    @Schema(description = "Nombre de la tarea", example = "Implementar login")
    private String name;

    @Schema(description = "Descripción de la tarea")
    private String description;

    @Schema(description = "Estado (PENDING, INPROGRESS, DONE, OVERDUE)")
    private String status;

    @Schema(description = "ID del usuario asignado (UUID)")
    private UUID assignedId;

    @Schema(description = "Nombre del usuario asignado", example = "Juan Pérez")
    private String assignedName;

    @Schema(description = "Fecha de inicio")
    private LocalDateTime startAt;

    @Schema(description = "Fecha de finalización")
    private LocalDateTime finishAt;

    @Schema(description = "Fecha de vencimiento")
    private LocalDateTime dueDate;

    @Schema(description = "Fecha de creación", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de actualización", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
