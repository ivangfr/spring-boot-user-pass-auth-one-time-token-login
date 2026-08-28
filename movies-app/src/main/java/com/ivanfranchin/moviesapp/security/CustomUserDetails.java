package com.ivanfranchin.moviesapp.security;

import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class CustomUserDetails implements UserDetails {

  private Long id;
  private String username;
  private String password;
  private String email;
  private String name;
  private Collection<? extends GrantedAuthority> authorities;
}
