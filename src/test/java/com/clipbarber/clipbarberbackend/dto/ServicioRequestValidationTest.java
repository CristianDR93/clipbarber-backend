package com.clipbarber.clipbarberbackend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ServicioRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        ServicioRequest request = new ServicioRequest("Corte", "Desc", 15000.0, 30, 1L);

        Set<ConstraintViolation<ServicioRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenNombreIsBlank() {
        ServicioRequest request = new ServicioRequest("", "Desc", 15000.0, 30, 1L);

        Set<ConstraintViolation<ServicioRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
    }

    @Test
    void shouldFailValidation_WhenPrecioIsNull() {
        ServicioRequest request = new ServicioRequest("Corte", "Desc", null, 30, 1L);

        Set<ConstraintViolation<ServicioRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("precio")));
    }

    @Test
    void shouldFailValidation_WhenPrecioIsNegative() {
        ServicioRequest request = new ServicioRequest("Corte", "Desc", -1.0, 30, 1L);

        Set<ConstraintViolation<ServicioRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("precio")));
    }

    @Test
    void shouldFailValidation_WhenDuracionIsNull() {
        ServicioRequest request = new ServicioRequest("Corte", "Desc", 15000.0, null, 1L);

        Set<ConstraintViolation<ServicioRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duracionMinutos")));
    }

    @Test
    void shouldFailValidation_WhenDuracionIsZero() {
        ServicioRequest request = new ServicioRequest("Corte", "Desc", 15000.0, 0, 1L);

        Set<ConstraintViolation<ServicioRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duracionMinutos")));
    }

    @Test
    void shouldFailValidation_WhenBarberiaIdIsNull() {
        ServicioRequest request = new ServicioRequest("Corte", "Desc", 15000.0, 30, null);

        Set<ConstraintViolation<ServicioRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("barberiaId")));
    }

    @Test
    void shouldReturnMultipleViolations_WhenAllRequiredFieldsAreInvalid() {
        ServicioRequest request = new ServicioRequest("", null, null, null, null);

        Set<ConstraintViolation<ServicioRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 4);
    }
}
