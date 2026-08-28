package com.ivanfranchin.moviesapp.user;

import com.ivanfranchin.moviesapp.security.Authorities;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User registerUser(String username, String password, String email) {
    User user = new User(username, passwordEncoder.encode(password), email, Authorities.USER);
    return userRepository.save(user);
  }

  public boolean existsByUsername(String username) {
    return userRepository.findByUsername(username).isPresent();
  }
}
