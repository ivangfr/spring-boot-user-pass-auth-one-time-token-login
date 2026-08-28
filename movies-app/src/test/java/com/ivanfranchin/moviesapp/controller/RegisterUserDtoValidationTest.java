package com.ivanfranchin.moviesapp.controller;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterUserDtoValidationTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @Test
  void validDtoNoViolations() {
    RegisterUserDto dto = new RegisterUserDto();
    dto.setUsername("john");
    dto.setPassword("password123");
    dto.setEmail("john@example.com");

    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

    assertThat(violations).isEmpty();
  }

  @Test
  void blankUsernameViolation() {
    RegisterUserDto dto = new RegisterUserDto();
    dto.setUsername("");
    dto.setPassword("password123");
    dto.setEmail("john@example.com");

    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("username");
    assertThat(violations.iterator().next().getMessage()).isEqualTo("Username is required");
  }

  @Test
  void nullUsernameViolation() {
    RegisterUserDto dto = new RegisterUserDto();
    dto.setPassword("password123");
    dto.setEmail("john@example.com");

    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("username");
  }

  @Test
  void blankPasswordViolation() {
    RegisterUserDto dto = new RegisterUserDto();
    dto.setUsername("john");
    dto.setPassword("");
    dto.setEmail("john@example.com");

    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("password");
    assertThat(violations.iterator().next().getMessage()).isEqualTo("Password is required");
  }

  @Test
  void blankEmailViolation() {
    RegisterUserDto dto = new RegisterUserDto();
    dto.setUsername("john");
    dto.setPassword("password123");
    dto.setEmail("");

    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
    assertThat(violations.iterator().next().getMessage()).isEqualTo("Email is required");
  }

  @Test
  void invalidEmailViolation() {
    RegisterUserDto dto = new RegisterUserDto();
    dto.setUsername("john");
    dto.setPassword("password123");
    dto.setEmail("invalid-email");

    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
    assertThat(violations.iterator().next().getMessage()).isEqualTo("Email must be valid");
  }

  @Test
  void multipleViolations() {
    RegisterUserDto dto = new RegisterUserDto();

    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

    assertThat(violations).hasSize(3);
  }
}
