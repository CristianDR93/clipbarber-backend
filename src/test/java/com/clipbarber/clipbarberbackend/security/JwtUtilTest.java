package com.clipbarber.clipbarberbackend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET = "d2ViYmVyYmFja2VuZFNlY3JldEtleUZvckpXVFRva2VuR2VuZXJhdGlvbjIwMjQ=";
    private static final long EXPIRATION = 86400000L;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, SECRET);

        Field expirationField = JwtUtil.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, EXPIRATION);
    }

    @Test
    void generateToken_ShouldReturnToken() {
        String token = jwtUtil.generateToken(1L, "juan@example.com", "CLIENTE");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractEmail_ShouldReturnCorrectEmail() {
        String token = jwtUtil.generateToken(1L, "juan@example.com", "CLIENTE");

        String email = jwtUtil.extractEmail(token);

        assertEquals("juan@example.com", email);
    }

    @Test
    void extractUserId_ShouldReturnCorrectUserId() {
        String token = jwtUtil.generateToken(42L, "test@example.com", "BARBERO");

        Long userId = jwtUtil.extractUserId(token);

        assertEquals(42L, userId);
    }

    @Test
    void extractRol_ShouldReturnCorrectRol() {
        String token = jwtUtil.generateToken(1L, "juan@example.com", "ADMIN");

        String rol = jwtUtil.extractRol(token);

        assertEquals("ADMIN", rol);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_WhenTokenIsValid() {
        String token = jwtUtil.generateToken(1L, "juan@example.com", "CLIENTE");

        boolean valid = jwtUtil.isTokenValid(token);

        assertTrue(valid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenTokenIsExpired() throws Exception {
        Field expirationField = JwtUtil.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, -1L);

        String token = jwtUtil.generateToken(1L, "juan@example.com", "CLIENTE");

        boolean valid = jwtUtil.isTokenValid(token);

        assertFalse(valid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenTokenIsInvalid() {
        boolean valid = jwtUtil.isTokenValid("invalid-token-string");

        assertFalse(valid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenTokenIsTampered() {
        String token = jwtUtil.generateToken(1L, "juan@example.com", "CLIENTE");
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";

        boolean valid = jwtUtil.isTokenValid(tampered);

        assertFalse(valid);
    }

    @Test
    void extractEmail_ShouldThrowException_WhenTokenIsInvalid() {
        assertThrows(Exception.class, () -> jwtUtil.extractEmail("bad-token"));
    }
}
