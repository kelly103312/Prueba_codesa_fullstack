package com.codesa.user.service.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {

    private Long id;
    private String name;
    private String description;
    private String status;
    private UUID assignedId;
    private String assignedName;
    private LocalDateTime startAt;
    private LocalDateTime finishAt;
}
