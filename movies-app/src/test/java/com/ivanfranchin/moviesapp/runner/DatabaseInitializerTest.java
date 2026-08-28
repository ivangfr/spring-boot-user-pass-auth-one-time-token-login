package com.ivanfranchin.moviesapp.runner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.ivanfranchin.moviesapp.user.User;
import com.ivanfranchin.moviesapp.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(DatabaseInitializer.class)
class DatabaseInitializerTest {

  @MockitoBean private UserService userService;

  @Autowired private DatabaseInitializer databaseInitializer;

  @Test
  void runWhenDatabaseEmptyCreatesAdmin() {
    given(userService.existsByUsername("admin")).willReturn(false);
    given(userService.registerUser(eq("admin"), eq("admin"), eq("admin@moviesapp.com")))
        .willAnswer(
            invocation -> {
              User user = new User("admin", "encoded_admin", "admin@moviesapp.com", "ADMIN");
              user.setId(1L);
              return user;
            });

    databaseInitializer.run();

    verify(userService).registerUser("admin", "admin", "admin@moviesapp.com");
  }

  @Test
  void runWhenDatabaseNotEmptyDoesNothing() {
    given(userService.existsByUsername("admin")).willReturn(true);

    databaseInitializer.run();

    verify(userService, never()).registerUser(any(), any(), any());
  }
}
