package com.ivanfranchin.moviesapp.security;

import com.ivanfranchin.moviesapp.controller.MoviesAppController;
import com.ivanfranchin.moviesapp.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoviesAppController.class)
@Import(SecurityConfig.class)
class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OneTimeTokenGenerationSuccessHandler oneTimeTokenGenerationSuccessHandler;

    @Test
    void getRootIsPublic() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void getRegisterIsPublic() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void getCheckEmailIsPublic() throws Exception {
        mockMvc.perform(get("/check-email"))
                .andExpect(status().isOk());
    }

    @Test
    void getMoviesWithoutAuthRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getUsersWithoutAuthRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void postPerformRegistrationIsPublic() throws Exception {
        mockMvc.perform(post("/perform-registration")
                        .with(csrf())
                        .param("username", "john")
                        .param("password", "password123")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void getMoviesWithUserRole() throws Exception {
        mockMvc.perform(get("/movies")
                        .with(user("user").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk());
    }

    @Test
    void getMoviesWithAdminRole() throws Exception {
        mockMvc.perform(get("/movies")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    void getUsersWithUserRoleForbidden() throws Exception {
        mockMvc.perform(get("/users")
                        .with(user("user").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUsersWithAdminRole() throws Exception {
        mockMvc.perform(get("/users")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk());
    }
}
