package com.ivanfranchin.moviesapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.ivanfranchin.moviesapp.security.Authorities;
import com.ivanfranchin.moviesapp.user.User;
import com.ivanfranchin.moviesapp.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MoviesAppController.class)
@AutoConfigureMockMvc(addFilters = false)
class MoviesAppControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @Test
  void getIndex() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
  }

  @Test
  void getRegister() throws Exception {
    mockMvc
        .perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("registerUserDto"));
  }

  @Test
  void getCheckEmail() throws Exception {
    mockMvc
        .perform(get("/check-email"))
        .andExpect(status().isOk())
        .andExpect(view().name("check-email"));
  }

  @Test
  void performRegistrationSuccess() throws Exception {
    User savedUser = new User("john", "encoded_password", "john@example.com", Authorities.USER);
    given(userService.registerUser(eq("john"), eq("password123"), eq("john@example.com")))
        .willReturn(savedUser);

    mockMvc
        .perform(
            post("/perform-registration")
                .param("username", "john")
                .param("password", "password123")
                .param("email", "john@example.com"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("response", "You've been registered successfully!"));

    verify(userService).registerUser("john", "password123", "john@example.com");
  }

  @Test
  void performRegistrationDuplicateUsername() throws Exception {
    given(userService.registerUser(any(), any(), any()))
        .willThrow(new DataIntegrityViolationException("duplicate key"));

    mockMvc
        .perform(
            post("/perform-registration")
                .param("username", "john")
                .param("password", "password123")
                .param("email", "john@example.com"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("response", "The username or email informed already exist!"));
  }

  @Test
  void performRegistrationGenericError() throws Exception {
    given(userService.registerUser(any(), any(), any()))
        .willThrow(new RuntimeException("database error"));

    mockMvc
        .perform(
            post("/perform-registration")
                .param("username", "john")
                .param("password", "password123")
                .param("email", "john@example.com"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("response", "An error occurred during registration!"));
  }

  @Test
  void performRegistrationBlankUsername() throws Exception {
    mockMvc
        .perform(
            post("/perform-registration")
                .param("username", "")
                .param("password", "password123")
                .param("email", "john@example.com"))
        .andExpect(status().isBadRequest());

    verify(userService, never()).registerUser(any(), any(), any());
  }

  @Test
  void performRegistrationInvalidEmail() throws Exception {
    mockMvc
        .perform(
            post("/perform-registration")
                .param("username", "john")
                .param("password", "password123")
                .param("email", "invalid-email"))
        .andExpect(status().isBadRequest());

    verify(userService, never()).registerUser(any(), any(), any());
  }
}
