package com.ivanfranchin.moviesapp.user;

import com.ivanfranchin.moviesapp.security.Authorities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import(UserService.class)
class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    void registerUserSuccess() {
        given(passwordEncoder.encode("password123")).willReturn("encoded_password");
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser("john", "password123", "john@example.com");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUserArg = captor.getValue();

        assertThat(savedUserArg.getUsername()).isEqualTo("john");
        assertThat(savedUserArg.getPassword()).isEqualTo("encoded_password");
        assertThat(savedUserArg.getEmail()).isEqualTo("john@example.com");
        assertThat(savedUserArg.getAuthority()).isEqualTo(Authorities.USER);
    }

    @Test
    void existsByUsernameReturnsTrueWhenFound() {
        User user = new User("john", "encoded_password", "john@example.com", Authorities.USER);
        user.setId(1L);
        given(userRepository.findByUsername("john")).willReturn(Optional.of(user));

        boolean exists = userService.existsByUsername("john");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsernameReturnsFalseWhenNotFound() {
        given(userRepository.findByUsername("unknown")).willReturn(Optional.empty());

        boolean exists = userService.existsByUsername("unknown");

        assertThat(exists).isFalse();
    }
}
