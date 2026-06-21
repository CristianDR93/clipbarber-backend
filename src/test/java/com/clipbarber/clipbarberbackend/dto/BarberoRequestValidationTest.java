package com.clipbarber.clipbarberbackend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BarberoRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        BarberoRequest request = new BarberoRequest(1L, 1L, "Corte", null, null);

        Set<ConstraintViolation<BarberoRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenUsuarioIdIsNull() {
        BarberoRequest request = new BarberoRequest(null, 1L, "Corte", null, null);

        Set<ConstraintViolation<BarberoRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("usuarioId")));
    }

    @Test
    void shouldFailValidation_WhenBarberiaIdIsNull() {
        BarberoRequest request = new BarberoRequest(1L, null, "Corte", null, null);

        Set<ConstraintViolation<BarberoRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("barberiaId")));
    }

    @Test
    void shouldReturnMultipleViolations_WhenBothIdsAreNull() {
        BarberoRequest request = new BarberoRequest(null, null, "Corte", null, null);

        Set<ConstraintViolation<BarberoRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 2);
    }
}
