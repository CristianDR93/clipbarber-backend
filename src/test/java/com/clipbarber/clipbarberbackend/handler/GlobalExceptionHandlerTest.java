package com.clipbarber.clipbarberbackend.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    void handleRuntimeException_ShouldReturnBadRequest() {
        RuntimeException exception = new RuntimeException("Error: Usuario no encontrado");

        ResponseEntity<?> response = handler.handleRuntimeException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Usuario no encontrado", response.getBody());
    }

    @Test
    void handleValidationExceptions_ShouldReturnFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("request", "email", "El email es obligatorio");
        FieldError fieldError2 = new FieldError("request", "password", "La contrasena es obligatoria");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                (MethodParameter) null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("El email es obligatorio", response.getBody().get("email"));
        assertEquals("La contrasena es obligatoria", response.getBody().get("password"));
    }

    @Test
    void handleValidationExceptions_ShouldReturnSingleFieldError() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("request", "name", "El nombre es obligatorio");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                (MethodParameter) null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("El nombre es obligatorio", response.getBody().get("name"));
    }
}
