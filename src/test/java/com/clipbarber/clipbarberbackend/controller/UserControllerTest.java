package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.LoginRequest;
import com.clipbarber.clipbarberbackend.dto.LoginResponse;
import com.clipbarber.clipbarberbackend.dto.RegisterRequest;
import com.clipbarber.clipbarberbackend.dto.UpdateUserRequest;
import com.clipbarber.clipbarberbackend.dto.UserResponse;
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
        assertInstanceOf(UserResponse.class, response.getBody());
        assertEquals("juan@example.com", ((UserResponse) response.getBody()).getEmail());
    }

    @Test
    void registerUser_ShouldReturnBadRequest_WhenEmailAlreadyRegistered() {
        when(userService.registerUser(registerRequest)).thenThrow(new RuntimeException("Error: Email ya esta registrado"));

        ResponseEntity<?> response = userController.registerUser(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Email ya esta registrado", response.getBody());
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("juan@example.com", "123456");
        LoginResponse loginResponse = new LoginResponse("jwt-token", 1L, "Juan Perez", "juan@example.com", "+56912345678", "CLIENTE");
        when(userService.login("juan@example.com", "123456")).thenReturn(loginResponse);

        ResponseEntity<?> response = userController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(LoginResponse.class, response.getBody());
        LoginResponse body = (LoginResponse) response.getBody();
        assertEquals("jwt-token", body.getToken());
        assertEquals("Bearer", body.getType());
        assertEquals("juan@example.com", body.getEmail());
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

        ResponseEntity<List<UserResponse>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("juan@example.com", response.getBody().get(0).getEmail());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(UserResponse.class, response.getBody());
        assertEquals("juan@example.com", ((UserResponse) response.getBody()).getEmail());
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenNotExists() {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        UpdateUserRequest request = new UpdateUserRequest("Juan Actualizado", null, null, "+56999999999", null);
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Juan Actualizado");
        updatedUser.setEmail("juan@example.com");
        updatedUser.setPhone("+56999999999");
        updatedUser.setRol(User.Rol.CLIENTE);
        when(userService.updateUser(1L, request)).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUser(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(UserResponse.class, response.getBody());
        assertEquals("Juan Actualizado", ((UserResponse) response.getBody()).getName());
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenUserNotFound() {
        UpdateUserRequest request = new UpdateUserRequest("Test", null, null, null, null);
        when(userService.updateUser(99L, request)).thenThrow(new RuntimeException("Error: Usuario no encontrado"));

        ResponseEntity<?> response = userController.updateUser(99L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Usuario no encontrado", response.getBody());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }

    @Test
    void deleteUser_ShouldReturnBadRequest_WhenUserNotFound() {
        doThrow(new RuntimeException("Error: Usuario no encontrado")).when(userService).deleteUser(99L);

        ResponseEntity<?> response = userController.deleteUser(99L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Usuario no encontrado", response.getBody());
    }
}
