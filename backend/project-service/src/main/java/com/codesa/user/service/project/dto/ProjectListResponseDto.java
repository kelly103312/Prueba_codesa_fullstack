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
public class ProjectListResponseDto {

    @Schema(description = "ID del proyecto", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del proyecto", example = "Sistema de Gestión")
    private String name;

    @Schema(description = "Descripción del proyecto")
    private String description;

    @Schema(description = "Estado del proyecto (ACTIVE, INACTIVE, COMPLETED)", example = "ACTIVE")
    private String status;

    @Schema(description = "Fecha de inicio")
    private LocalDateTime startAt;

    @Schema(description = "Fecha de finalización")
    private LocalDateTime finishAt;

    @Schema(description = "ID del propietario (UUID)")
    private UUID ownerId;

    @Schema(description = "Nombre del propietario", example = "Admin")
    private String ownerName;
    
    @Schema(description = "ID del usuario asignado (UUID)")
    private UUID assignedId;

    @Schema(description = "Nombre del usuario asignado", example = "Juan Pérez")
    private String assignedName;
}
