package com.ivanfranchin.moviesapp.runner;

import com.ivanfranchin.moviesapp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        if (!userService.existsByUsername("admin")) {
            userService.registerUser("admin", "admin", "admin@moviesapp.com");
            log.info("The Movie App admin was created.");
        }
    }
}
