package com.ivanfranchin.moviesapp.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.ivanfranchin.moviesapp.email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.ott.DefaultOneTimeToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(MyOneTimeTokenGenerationSuccessHandler.class)
class MyOneTimeTokenGenerationSuccessHandlerTest {

  @MockitoBean private CustomUserDetailsService customUserDetailsService;

  @MockitoBean private EmailService emailService;

  @Autowired private MyOneTimeTokenGenerationSuccessHandler handler;

  @Test
  void handleSuccess() throws IOException {
    CustomUserDetails userDetails = new CustomUserDetails();
    userDetails.setId(1L);
    userDetails.setUsername("john");
    userDetails.setEmail("john@example.com");

    DefaultOneTimeToken oneTimeToken =
        new DefaultOneTimeToken("test-token-123", "john", Instant.now().plusSeconds(600));

    HttpServletRequest request = createMockRequest("http://localhost:8025/login/ott");
    HttpServletResponse response = createMockResponse();

    given(customUserDetailsService.loadUserByUsername("john")).willReturn(userDetails);

    handler.handle(request, response, oneTimeToken);

    ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
    verify(emailService)
        .sendEmail(emailCaptor.capture(), subjectCaptor.capture(), bodyCaptor.capture());

    assertThat(emailCaptor.getValue()).isEqualTo("john@example.com");
    assertThat(subjectCaptor.getValue()).isEqualTo("Your One Time Token for Movies App");
    assertThat(bodyCaptor.getValue()).contains("token=test-token-123");
    assertThat(bodyCaptor.getValue()).contains("/login/ott");

    verify(response).sendRedirect("/check-email");
  }

  @Test
  void handleFailure() throws IOException {
    DefaultOneTimeToken oneTimeToken =
        new DefaultOneTimeToken("test-token-123", "john", Instant.now().plusSeconds(600));

    HttpServletRequest request = createMockRequest("http://localhost:8025/login/ott");
    HttpServletResponse response = createMockResponse();

    given(customUserDetailsService.loadUserByUsername("john"))
        .willThrow(new RuntimeException("User not found"));

    handler.handle(request, response, oneTimeToken);

    verify(response).sendRedirect("/login?error");
    verify(emailService, never()).sendEmail(any(), any(), any());
  }

  private HttpServletRequest createMockRequest(String requestUrl) {
    HttpServletRequest request = mock(HttpServletRequest.class);
    given(request.getContextPath()).willReturn("");
    given(request.getServerName()).willReturn("localhost");
    given(request.getServerPort()).willReturn(8025);
    given(request.getScheme()).willReturn("http");
    given(request.getRequestURI()).willReturn("/login/ott");
    return request;
  }

  private HttpServletResponse createMockResponse() {
    return mock(HttpServletResponse.class);
  }
}
