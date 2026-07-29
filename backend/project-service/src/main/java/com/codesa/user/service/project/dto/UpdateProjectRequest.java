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
public class UpdateProjectRequest {

    @Schema(description = "ID del proyecto")
    private Long id;
    @Schema(description = "Nuevo nombre")
    private String name;
    @Schema(description = "Nueva descripción")
    private String description;
    @Schema(description = "Nuevo estado (ACTIVE, ARCHIVED, CLOSED)") 
    private String status;
    @Schema(description = "ID del usuario asignado", example = "123e4567-e89b-..")
    private UUID assignedId;
    @Schema(description = "Nombre del usuario asignado", example = "Juan Perez")
    private String assignedName;
    @Schema(description = "Fecha de inicio del proyecto")
    private LocalDateTime startAt;
    @Schema(description = "Fecha de finalización del proyecto")
    private LocalDateTime finishAt;
}
