package com.subscribe.demo.service.event;

import com.subscribe.demo.endpoint.event.model.TestEventTriggered;
import com.subscribe.demo.mail.Email;
import com.subscribe.demo.mail.Mailer;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TestEventTriggeredService implements Consumer<TestEventTriggered> {
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(TestEventTriggered event) {
    var recipient = new InternetAddress(event.getTo());
    mailer.accept(
        new Email(
            recipient,
            List.of(),
            List.of(),
            "Test event",
            "Ceci teste juste l'event, sans DB",
            List.of()));
  }
}
