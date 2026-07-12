package com.subscribe.demo.service.event;

import com.subscribe.demo.endpoint.event.model.SubscriptionConfirmedRequest;
import com.subscribe.demo.mail.Email;
import com.subscribe.demo.mail.Mailer;
import com.subscribe.demo.subscribe.entity.Subscribe;
import com.subscribe.demo.subscribe.repository.SubscriptionRepository;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionConfirmedRequestService implements Consumer<SubscriptionConfirmedRequest> {

  private final SubscriptionRepository subscribeRepository;
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(SubscriptionConfirmedRequest event) {
    Subscribe subscribe = subscribeRepository.findById(event.getSubscribeId()).orElseThrow();

    var recipient = new InternetAddress(subscribe.getUser().getEmail());
    var body =
        "Bonjour "
            + subscribe.getUser().getFirstname()
            + ", votre inscription au cours \""
            + subscribe.getCourse().getTitle()
            + "\" est confirmée !";

    mailer.accept(
        new Email(recipient, List.of(), List.of(), "Inscription confirmée", body, List.of()));
  }
}
