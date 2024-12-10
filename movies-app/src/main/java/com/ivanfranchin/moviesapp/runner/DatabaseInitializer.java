package com.ivanfranchin.moviesapp.runner;

import com.ivanfranchin.moviesapp.user.User;
import com.ivanfranchin.moviesapp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User("admin", passwordEncoder.encode("admin"), "admin@moviesapp.com", "ADMIN");
            userRepository.save(admin);
            log.info("The Movie App admin was created.");
        }
    }
}
