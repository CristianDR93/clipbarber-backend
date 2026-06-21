package com.clipbarber.clipbarberbackend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CitaRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        CitaRequest request = new CitaRequest(1L, 1L, LocalDateTime.now().plusDays(1), null, 15000.0);

        Set<ConstraintViolation<CitaRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenBarberoIdIsNull() {
        CitaRequest request = new CitaRequest(null, 1L, LocalDateTime.now().plusDays(1), null, 15000.0);

        Set<ConstraintViolation<CitaRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("barberoId")));
    }

    @Test
    void shouldFailValidation_WhenServicioIdIsNull() {
        CitaRequest request = new CitaRequest(1L, null, LocalDateTime.now().plusDays(1), null, 15000.0);

        Set<ConstraintViolation<CitaRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("servicioId")));
    }

    @Test
    void shouldFailValidation_WhenFechaHoraIsNull() {
        CitaRequest request = new CitaRequest(1L, 1L, null, null, 15000.0);

        Set<ConstraintViolation<CitaRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fechaHora")));
    }

    @Test
    void shouldFailValidation_WhenPrecioIsNull() {
        CitaRequest request = new CitaRequest(1L, 1L, LocalDateTime.now().plusDays(1), null, null);

        Set<ConstraintViolation<CitaRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("precio")));
    }

    @Test
    void shouldReturnMultipleViolations_WhenAllRequiredFieldsAreNull() {
        CitaRequest request = new CitaRequest(null, null, null, null, null);

        Set<ConstraintViolation<CitaRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 4);
    }
}
