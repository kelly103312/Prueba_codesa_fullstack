package com.codesa.user.service.demo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String error,
    String message,
    int status,
    LocalDateTime timestamp,
    Map<String, String> fields
) {
    public ErrorResponse(String error, String message, int status) {
        this(error, message, status, LocalDateTime.now(), null);
    }

    public ErrorResponse(String error, String message, int status, Map<String, String> fields) {
        this(error, message, status, LocalDateTime.now(), fields);
    }
}
