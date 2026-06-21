package com.clipbarber.clipbarberbackend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        LoginRequest request = new LoginRequest("juan@example.com", "123456");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenEmailIsBlank() {
        LoginRequest request = new LoginRequest("", "123456");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidation_WhenEmailIsInvalid() {
        LoginRequest request = new LoginRequest("not-an-email", "123456");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidation_WhenPasswordIsBlank() {
        LoginRequest request = new LoginRequest("juan@example.com", "");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void shouldReturnMultipleViolations_WhenAllFieldsAreInvalid() {
        LoginRequest request = new LoginRequest("", "");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 2);
    }
}
