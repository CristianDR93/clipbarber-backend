package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.LoginRequest;
import com.clipbarber.clipbarberbackend.dto.RegisterRequest;
import com.clipbarber.clipbarberbackend.model.User;
import com.clipbarber.clipbarberbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Juan Perez");
        user.setEmail("juan@example.com");
        user.setPassword("hashed123");
        user.setPhone("+56912345678");
        user.setRol(User.Rol.CLIENTE);

        registerRequest = new RegisterRequest("Juan Perez", "juan@example.com", "123456", "+56912345678", User.Rol.CLIENTE);
    }

    @Test
    void registerUser_ShouldReturnCreatedUser() {
        when(userService.registerUser(registerRequest)).thenReturn(user);

        ResponseEntity<?> response = userController.registerUser(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(User.class, response.getBody());
        assertEquals("juan@example.com", ((User) response.getBody()).getEmail());
    }

    @Test
    void registerUser_ShouldReturnBadRequest_WhenEmailAlreadyRegistered() {
        when(userService.registerUser(registerRequest)).thenThrow(new RuntimeException("Error: Email ya esta registrado"));

        ResponseEntity<?> response = userController.registerUser(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Email ya esta registrado", response.getBody());
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("juan@example.com", "123456");
        when(userService.login("juan@example.com", "123456")).thenReturn(user);

        ResponseEntity<?> response = userController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(User.class, response.getBody());
        assertEquals("juan@example.com", ((User) response.getBody()).getEmail());
    }

    @Test
    void login_ShouldReturnBadRequest_WhenCredentialsAreInvalid() {
        LoginRequest request = new LoginRequest("juan@example.com", "wrong");
        when(userService.login("juan@example.com", "wrong"))
                .thenThrow(new RuntimeException("Error: Credenciales invalidas"));

        ResponseEntity<?> response = userController.login(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Credenciales invalidas", response.getBody());
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        List<User> users = List.of(user);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("juan@example.com", response.getBody().get(0).getEmail());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(User.class, response.getBody());
        assertEquals("juan@example.com", ((User) response.getBody()).getEmail());
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenNotExists() {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
