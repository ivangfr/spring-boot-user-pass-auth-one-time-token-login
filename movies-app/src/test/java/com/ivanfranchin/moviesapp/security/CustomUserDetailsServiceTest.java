package com.ivanfranchin.moviesapp.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.ivanfranchin.moviesapp.user.User;
import com.ivanfranchin.moviesapp.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(CustomUserDetailsService.class)
class CustomUserDetailsServiceTest {

  @MockitoBean private UserRepository userRepository;

  @Autowired private CustomUserDetailsService customUserDetailsService;

  @Test
  void loadUserByUsernameSuccess() {
    User user = new User("john", "encoded_password", "john@example.com", Authorities.USER);
    user.setId(1L);
    given(userRepository.findByUsername("john")).willReturn(Optional.of(user));

    CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername("john");

    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getId()).isEqualTo(1L);
    assertThat(userDetails.getUsername()).isEqualTo("john");
    assertThat(userDetails.getPassword()).isEqualTo("encoded_password");
    assertThat(userDetails.getEmail()).isEqualTo("john@example.com");
    assertThat(userDetails.getName()).isEqualTo("john");
    assertThat(userDetails.getAuthorities()).hasSize(1);
    assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
        .isEqualTo(Authorities.USER);
  }

  @Test
  void loadUserByUsernameNotFound() {
    given(userRepository.findByUsername("unknown")).willReturn(Optional.empty());

    assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("unknown"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("unknown");
  }

  @Test
  void loadUserByUsernameAdminAuthority() {
    User admin =
        new User("admin", "encoded_admin_password", "admin@example.com", Authorities.ADMIN);
    admin.setId(1L);
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(admin));

    CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

    assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
        .isEqualTo(Authorities.ADMIN);
  }
}
