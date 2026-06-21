package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.LoginResponse;
import com.clipbarber.clipbarberbackend.dto.RegisterRequest;
import com.clipbarber.clipbarberbackend.dto.UpdateUserRequest;
import com.clipbarber.clipbarberbackend.model.User;
import com.clipbarber.clipbarberbackend.repository.UserRepository;
import com.clipbarber.clipbarberbackend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User user;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Juan Pérez");
        user.setEmail("juan@example.com");
        user.setPassword("hashedPassword");
        user.setPhone("+56912345678");
        user.setRol(User.Rol.CLIENTE);

        registerRequest = new RegisterRequest("Juan Pérez", "juan@example.com", "123456", "+56912345678", User.Rol.CLIENTE);
    }

    @Test
    void registerUser_ShouldSaveUser_WhenEmailNotRegistered() {
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals("juan@example.com", result.getEmail());
        assertEquals("Juan Pérez", result.getName());
        assertEquals("hashedPassword", result.getPassword());
        assertEquals(User.Rol.CLIENTE, result.getRol());
        verify(userRepository).findByEmail("juan@example.com");
        verify(userRepository).save(any());
    }

    @Test
    void registerUser_ShouldAssignDefaultRol_WhenRolIsNull() {
        RegisterRequest requestWithoutRol = new RegisterRequest("Juan", "juan2@example.com", "123456", "+56912345678", null);
        when(userRepository.findByEmail("juan2@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(requestWithoutRol);

        assertEquals(User.Rol.CLIENTE, result.getRol());
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyRegistered() {
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(registerRequest));

        assertEquals("Error: Email ya esta registrado", exception.getMessage());
        verify(userRepository).findByEmail("juan@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(99L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(99L);
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "juan@example.com", "CLIENTE")).thenReturn("mock-jwt-token");

        LoginResponse result = userService.login("juan@example.com", "123456");

        assertNotNull(result);
        assertEquals("mock-jwt-token", result.getToken());
        assertEquals("Bearer", result.getType());
        assertEquals(1L, result.getUserId());
        assertEquals("Juan Pérez", result.getName());
        assertEquals("juan@example.com", result.getEmail());
        assertEquals("CLIENTE", result.getRol());
        verify(userRepository).findByEmail("juan@example.com");
        verify(passwordEncoder).matches("123456", "hashedPassword");
        verify(jwtUtil).generateToken(1L, "juan@example.com", "CLIENTE");
    }

    @Test
    void login_ShouldThrowException_WhenEmailNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.login("unknown@example.com", "123456"));

        assertEquals("Error: Credenciales invalidas", exception.getMessage());
        verify(userRepository).findByEmail("unknown@example.com");
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsWrong() {
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.login("juan@example.com", "wrongpassword"));

        assertEquals("Error: Credenciales invalidas", exception.getMessage());
        verify(userRepository).findByEmail("juan@example.com");
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenExists() {
        UpdateUserRequest request = new UpdateUserRequest("Juan Actualizado", null, null, "+56999999999", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, request);

        assertNotNull(result);
        assertEquals("Juan Actualizado", result.getName());
        assertEquals("+56999999999", result.getPhone());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any());
    }

    @Test
    void updateUser_ShouldUpdateEmail_WhenEmailNotTaken() {
        UpdateUserRequest request = new UpdateUserRequest(null, "nuevo@email.com", null, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("nuevo@email.com")).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, request);

        assertEquals("nuevo@email.com", result.getEmail());
        verify(userRepository).findByEmail("nuevo@email.com");
    }

    @Test
    void updateUser_ShouldThrowException_WhenEmailAlreadyRegistered() {
        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setEmail("otro@email.com");
        UpdateUserRequest request = new UpdateUserRequest(null, "otro@email.com", null, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("otro@email.com")).thenReturn(Optional.of(existingUser));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(1L, request));

        assertEquals("Error: Email ya esta registrado", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        UpdateUserRequest request = new UpdateUserRequest("Test", null, null, null, null);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(99L, request));

        assertEquals("Error: Usuario no encontrado", exception.getMessage());
    }

    @Test
    void updateUser_ShouldEncodePassword_WhenPasswordProvided() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, "newPassword123", null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, request);

        assertEquals("encodedNewPassword", result.getPassword());
        verify(passwordEncoder).encode("newPassword123");
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(99L));

        assertEquals("Error: Usuario no encontrado", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }
}
