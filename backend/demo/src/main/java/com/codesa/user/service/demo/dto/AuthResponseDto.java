package com.codesa.user.service.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthResponseDto {
    @Schema(description = "Datos del usuario autenticado")
    private UserDto user;
    @Schema(description = "Token JWT de acceso", example = "ey.........")
    private String token;

    public AuthResponseDto() {
    }

    public AuthResponseDto(UserDto user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
