package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void fromUser_ShouldMapAllFields() {
        User user = new User();
        user.setId(1L);
        user.setName("Juan Perez");
        user.setEmail("juan@example.com");
        user.setPhone("+56912345678");
        user.setRol(User.Rol.CLIENTE);

        UserResponse response = UserResponse.fromUser(user);

        assertEquals(1L, response.getId());
        assertEquals("Juan Perez", response.getName());
        assertEquals("juan@example.com", response.getEmail());
        assertEquals("+56912345678", response.getPhone());
        assertEquals("CLIENTE", response.getRol());
    }

    @Test
    void fromUser_ShouldNotExposePassword() {
        User user = new User();
        user.setId(1L);
        user.setName("Juan Perez");
        user.setEmail("juan@example.com");
        user.setPassword("secret123");
        user.setPhone("+56912345678");
        user.setRol(User.Rol.CLIENTE);

        UserResponse response = UserResponse.fromUser(user);

        String json = response.toString();
        assertFalse(json.contains("secret123"), "UserResponse no debe contener la contraseña");
    }

    @Test
    void fromUser_ShouldMapBarberoRole() {
        User user = new User();
        user.setId(2L);
        user.setName("Pedro Barbero");
        user.setEmail("pedro@example.com");
        user.setPhone("+56987654321");
        user.setRol(User.Rol.BARBERO);

        UserResponse response = UserResponse.fromUser(user);

        assertEquals("BARBERO", response.getRol());
    }

    @Test
    void fromUser_ShouldMapAdminRole() {
        User user = new User();
        user.setId(3L);
        user.setName("Admin");
        user.setEmail("admin@example.com");
        user.setPhone("+56900000000");
        user.setRol(User.Rol.ADMIN);

        UserResponse response = UserResponse.fromUser(user);

        assertEquals("ADMIN", response.getRol());
    }

    @Test
    void constructor_ShouldCreateEmptyObject() {
        UserResponse response = new UserResponse();

        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getEmail());
        assertNull(response.getPhone());
        assertNull(response.getRol());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        UserResponse response = new UserResponse(1L, "Juan", "juan@example.com", "+56912345678", "CLIENTE");

        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getName());
        assertEquals("juan@example.com", response.getEmail());
        assertEquals("+56912345678", response.getPhone());
        assertEquals("CLIENTE", response.getRol());
    }

    @Test
    void setters_ShouldWork() {
        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("Test");
        response.setEmail("test@example.com");
        response.setPhone("123");
        response.setRol("ADMIN");

        assertEquals(1L, response.getId());
        assertEquals("Test", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("123", response.getPhone());
        assertEquals("ADMIN", response.getRol());
    }
}
