package com.codesa.user.service.demo.security;

public record AuthenticatedUser(String userId, String email, String role) {
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}