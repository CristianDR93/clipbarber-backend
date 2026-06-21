package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        UpdateUserRequest request = new UpdateUserRequest("Juan", "juan@example.com", "123456", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreNull() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, null, null, null);

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenNameIsTooShort() {
        UpdateUserRequest request = new UpdateUserRequest("J", null, null, null, null);

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldFailValidation_WhenEmailIsInvalid() {
        UpdateUserRequest request = new UpdateUserRequest(null, "not-an-email", null, null, null);

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidation_WhenPasswordIsTooShort() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, "12345", null, null);

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void shouldPassValidation_WhenPasswordHasExactly6Chars() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, "123456", null, null);

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldReturnMultipleViolations_WhenMultipleFieldsAreInvalid() {
        UpdateUserRequest request = new UpdateUserRequest("J", "bad-email", "12345", null, null);

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 3);
    }
}
