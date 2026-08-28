package com.ivanfranchin.moviesapp.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(EmailService.class)
class EmailServiceTest {

  @MockitoBean private JavaMailSender mailSender;

  @Autowired private EmailService emailService;

  @Test
  void sendEmailSuccess() {
    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

    emailService.sendEmail("to@example.com", "Test Subject", "Test Body");

    verify(mailSender).send(captor.capture());
    SimpleMailMessage sentMessage = captor.getValue();
    assertThat(sentMessage.getFrom()).isEqualTo("noreply@moviesapp.com");
    assertThat(sentMessage.getTo()).containsExactly("to@example.com");
    assertThat(sentMessage.getSubject()).isEqualTo("Test Subject");
    assertThat(sentMessage.getText()).isEqualTo("Test Body");
  }

  @Test
  void sendEmailFailureThrowsException() {
    doThrow(new MailSendException("Mail server unavailable"))
        .when(mailSender)
        .send(any(SimpleMailMessage.class));

    assertThatThrownBy(() -> emailService.sendEmail("to@example.com", "Subject", "Body"))
        .isInstanceOf(MailSendException.class)
        .hasMessage("Mail server unavailable");
  }

  @Test
  void sendEmailFromAddressAlwaysNoreply() {
    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

    emailService.sendEmail("anyone@example.com", "Subject", "Body");

    verify(mailSender).send(captor.capture());
    assertThat(captor.getValue().getFrom()).isEqualTo("noreply@moviesapp.com");
  }
}
