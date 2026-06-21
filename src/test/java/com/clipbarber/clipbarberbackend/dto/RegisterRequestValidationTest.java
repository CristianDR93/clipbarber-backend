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

class RegisterRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        RegisterRequest request = new RegisterRequest("Juan Perez", "juan@example.com", "123456", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenNameIsBlank() {
        RegisterRequest request = new RegisterRequest("", "juan@example.com", "123456", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldFailValidation_WhenNameIsNull() {
        RegisterRequest request = new RegisterRequest(null, "juan@example.com", "123456", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldFailValidation_WhenEmailIsBlank() {
        RegisterRequest request = new RegisterRequest("Juan", "", "123456", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidation_WhenEmailIsInvalid() {
        RegisterRequest request = new RegisterRequest("Juan", "not-an-email", "123456", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidation_WhenPasswordIsBlank() {
        RegisterRequest request = new RegisterRequest("Juan", "juan@example.com", "", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void shouldFailValidation_WhenPasswordIsTooShort() {
        RegisterRequest request = new RegisterRequest("Juan", "juan@example.com", "12345", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void shouldPassValidation_WhenPasswordHasExactly6Chars() {
        RegisterRequest request = new RegisterRequest("Juan", "juan@example.com", "123456", "+56912345678", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenPhoneIsBlank() {
        RegisterRequest request = new RegisterRequest("Juan", "juan@example.com", "123456", "", User.Rol.CLIENTE);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
    }

    @Test
    void shouldPassValidation_WhenRolIsNull() {
        RegisterRequest request = new RegisterRequest("Juan", "juan@example.com", "123456", "+56912345678", null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldReturnMultipleViolations_WhenMultipleFieldsAreInvalid() {
        RegisterRequest request = new RegisterRequest("", "", "12", "", null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 4);
    }
}
