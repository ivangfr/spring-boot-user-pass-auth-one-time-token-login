package com.ivanfranchin.moviesapp.security;

import com.ivanfranchin.moviesapp.email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class MyOneTimeTokenGenerationSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    private final CustomUserDetailsService customUserDetailsService;
    private final EmailService emailService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(UrlUtils.buildFullRequestUrl(request))
                    .replacePath(request.getContextPath())
                    .replaceQuery(null)
                    .fragment(null)
                    .path("/login/ott")
                    .queryParam("token", oneTimeToken.getTokenValue());

            String magicLink = builder.toUriString();
            String email = getUserEmail(oneTimeToken.getUsername());
            writeAndSendEmail(magicLink, email);

            response.sendRedirect("/check-email");
        } catch (Exception e) {
            response.sendRedirect("/login?error");
        }
    }

    private void writeAndSendEmail(String magicLink, String email) {
        String subject = "Your One Time Token for Movies App";
        String body = "Use the following link to sign in: " + magicLink;
        emailService.sendEmail(email, subject, body);
    }

    private String getUserEmail(String username) {
        CustomUserDetails user = customUserDetailsService.loadUserByUsername(username);
        return user.getEmail();
    }
}