package com.clipbarber.clipbarberbackend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    void constructor_ShouldSetAllFields() {
        LoginResponse response = new LoginResponse("jwt-token", 1L, "Juan", "juan@example.com", "+56912345678", "CLIENTE");

        assertEquals("jwt-token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("Juan", response.getName());
        assertEquals("juan@example.com", response.getEmail());
        assertEquals("+56912345678", response.getPhone());
        assertEquals("CLIENTE", response.getRol());
    }

    @Test
    void defaultType_ShouldBeBearer() {
        LoginResponse response = new LoginResponse();

        assertEquals("Bearer", response.getType());
    }

    @Test
    void emptyConstructor_ShouldCreateEmptyObject() {
        LoginResponse response = new LoginResponse();

        assertNull(response.getToken());
        assertNull(response.getUserId());
        assertNull(response.getName());
        assertNull(response.getEmail());
        assertNull(response.getPhone());
        assertNull(response.getRol());
    }

    @Test
    void setters_ShouldWork() {
        LoginResponse response = new LoginResponse();
        response.setToken("new-token");
        response.setUserId(2L);
        response.setName("Pedro");
        response.setEmail("pedro@example.com");
        response.setPhone("+56999999999");
        response.setRol("BARBERO");
        response.setType("Bearer");

        assertEquals("new-token", response.getToken());
        assertEquals(2L, response.getUserId());
        assertEquals("Pedro", response.getName());
        assertEquals("pedro@example.com", response.getEmail());
        assertEquals("+56999999999", response.getPhone());
        assertEquals("BARBERO", response.getRol());
        assertEquals("Bearer", response.getType());
    }
}
