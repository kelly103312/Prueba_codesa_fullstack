package com.codesa.user.service.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    @Schema(description = "Estado de operacion", example = "200", accessMode = Schema.AccessMode.READ_ONLY)
    private int status;
    
    @Schema(description = "Mensaje detallado de la operacion", example = "Creación exitosa", accessMode = Schema.AccessMode.READ_ONLY)
    private String message;

    @Schema(description = "Datos de la consulta", example = "{id: 1, projectID: 2 ...}", accessMode = Schema.AccessMode.READ_ONLY)
    private T data;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .date(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .status(201)
                .message(message)
                .data(data)
                .date(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .date(LocalDateTime.now())
                .build();
    }
}
