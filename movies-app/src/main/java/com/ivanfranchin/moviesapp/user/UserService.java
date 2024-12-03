package com.ivanfranchin.moviesapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User validateAndGetByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username %s not found".formatted(username)));
    }

    public User saveUser(String username, String password, String email) {
        User user = new User(username, password, email, "ROLE_USER");
        return userRepository.save(user);
    }
}
