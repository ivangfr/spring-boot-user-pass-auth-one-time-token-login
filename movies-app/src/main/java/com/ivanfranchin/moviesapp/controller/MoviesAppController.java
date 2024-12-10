package com.ivanfranchin.moviesapp.controller;

import com.ivanfranchin.moviesapp.user.User;
import com.ivanfranchin.moviesapp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MoviesAppController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerUserDto", new RegisterUserDto());
        return "register";
    }

    @PostMapping("/perform-registration")
    public String performRegistration(Model model, @ModelAttribute RegisterUserDto registerUserDto) {
        String message;
        try {
            User user = userService.saveUser(
                    registerUserDto.getUsername(),
                    passwordEncoder.encode(registerUserDto.getPassword()),
                    registerUserDto.getEmail());
            log.info("User {} registered successfully", user);
            message = "You've been registered successfully!";
        } catch (DataIntegrityViolationException e) {
            log.error("An error occurred while registering user {}", registerUserDto, e);
            message = "The username or email informed already exist!";
        } catch (Exception e) {
            log.error("An error occurred while registering user {}", registerUserDto, e);
            message = "An error occurred during registration!";
        }
        model.addAttribute("response", message);
        return "index";
    }

    @GetMapping("/check-email")
    public String checkEmail() {
        return "check-email";
    }

    @GetMapping("/movies")
    public String movies() {
        return "movies";
    }
}