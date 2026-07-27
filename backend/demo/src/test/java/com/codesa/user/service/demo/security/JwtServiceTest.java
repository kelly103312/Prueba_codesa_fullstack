package com.codesa.user.service.demo.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceTest {

    @Test
    void shouldExtractUserIdAndRoleFromGeneratedToken() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-1234567890");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);

        String token = jwtService.generateToken("user-123", "admin@test.com", "ADMIN");

        assertEquals("user-123", jwtService.extractUserId(token));
        assertEquals("ADMIN", jwtService.extractRole(token));
    }
}
