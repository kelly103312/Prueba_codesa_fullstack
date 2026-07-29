package com.codesa.user.service.demo.dto;
import io.swagger.v3.oas.annotations.media.Schema;

public class AuthenticatedRequestDto {
    @Schema(description = "Correo electrónico del usuario", example = "admin@codesa.com")
    private String email;
    @Schema(description = "Contraseña hasheada con SHA-256 (enviar el hash, no texto plano)", example = "a1b2c3d4e5f6...")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
