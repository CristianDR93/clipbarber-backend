package com.clipbarber.clipbarberbackend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BarberiaRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        BarberiaRequest request = new BarberiaRequest("BarberShop", "Av. Principal 123", "+56912345678", "shop@example.com", "url.jpg", -33.4, -70.6);

        Set<ConstraintViolation<BarberiaRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenNombreIsBlank() {
        BarberiaRequest request = new BarberiaRequest("", "Av. Principal", null, null, null, -33.4, -70.6);

        Set<ConstraintViolation<BarberiaRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
    }

    @Test
    void shouldFailValidation_WhenDireccionIsBlank() {
        BarberiaRequest request = new BarberiaRequest("BarberShop", "", null, null, null, -33.4, -70.6);

        Set<ConstraintViolation<BarberiaRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("direccion")));
    }

    @Test
    void shouldFailValidation_WhenLatitudIsNull() {
        BarberiaRequest request = new BarberiaRequest("BarberShop", "Av. Principal", null, null, null, null, -70.6);

        Set<ConstraintViolation<BarberiaRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("latitud")));
    }

    @Test
    void shouldFailValidation_WhenLongitudIsNull() {
        BarberiaRequest request = new BarberiaRequest("BarberShop", "Av. Principal", null, null, null, -33.4, null);

        Set<ConstraintViolation<BarberiaRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("longitud")));
    }

    @Test
    void shouldReturnMultipleViolations_WhenAllRequiredFieldsAreInvalid() {
        BarberiaRequest request = new BarberiaRequest("", "", null, null, null, null, null);

        Set<ConstraintViolation<BarberiaRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 4);
    }
}
