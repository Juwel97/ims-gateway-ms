package com.ims.common;

import com.ims.common.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void shouldGenerateAndParseAccessToken() {
        String token = jwtService.generateAccessToken("user@example.com", "STUDENT");

        assertNotNull(token);
        assertFalse(token.isBlank());

        Claims claims = jwtService.parseToken(token);
        assertEquals("user@example.com", claims.getSubject());
        assertEquals("STUDENT", claims.get("role", String.class));
    }

    @Test
    void shouldGenerateRefreshToken() {
        String token = jwtService.generateRefreshToken("admin@example.com", "ADMIN");

        assertNotNull(token);
        assertFalse(token.isBlank());

        Claims claims = jwtService.parseToken(token);
        assertEquals("admin@example.com", claims.getSubject());
        assertEquals("ADMIN", claims.get("role", String.class));
    }
}
