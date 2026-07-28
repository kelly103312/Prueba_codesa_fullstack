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
public class CreateProjectRequest {
    @Schema(description = "Nombre del proyecto", example = "Sistema de Gestión")
    private String name;
    @Schema(description = "Descripción del proyecto", example = "Proyecto para gestionar...")
    private String description;
    @Schema(description = "ID del usuario asignado", example = "123e4567-e89b-..")
    private UUID assignedId;
    @Schema(description = "Nombre del usuario asignado", example = "Juan Perez")
    private String assignedName;
    @Schema(description = "Fecha de inicio del proyecto")
    private LocalDateTime startAt;
    @Schema(description = "Fecha de finalización del proyecto")
    private LocalDateTime finishAt;
}
