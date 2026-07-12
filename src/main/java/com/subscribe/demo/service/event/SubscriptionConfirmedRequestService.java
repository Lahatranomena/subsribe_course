package com.subscribe.demo.service.event;

import static java.io.File.createTempFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subscribe.demo.endpoint.event.model.SubscriptionConfirmedRequest;
import com.subscribe.demo.file.bucket.BucketComponent;
import com.subscribe.demo.mail.Email;
import com.subscribe.demo.mail.Mailer;
import com.subscribe.demo.subscribe.entity.Subscribe;
import com.subscribe.demo.subscribe.repository.SubscriptionRepository;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SubscriptionConfirmedRequestService implements Consumer<SubscriptionConfirmedRequest> {

  private final SubscriptionRepository subscribeRepository;
  private final Mailer mailer;
  private final BucketComponent bucketComponent;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  @Transactional
  @Override
  public void accept(SubscriptionConfirmedRequest event) {
    Subscribe subscribe = subscribeRepository.findById(event.getSubscribeId()).orElseThrow();

    String ticketUrl = generateAndUploadTicket(subscribe);

    var recipient = new InternetAddress(subscribe.getUser().getEmail());
    var body =
        "<p>Bonjour "
            + subscribe.getUser().getFirstname()
            + ", votre inscription au cours \""
            + subscribe.getCourse().getTitle()
            + "\" est confirmée !</p>"
            + "<p><a href=\""
            + ticketUrl
            + "\">Télécharger votre ticket d'inscription (JSON)</a></p>";

    mailer.accept(
        new Email(recipient, List.of(), List.of(), "Inscription confirmée", body, List.of()));
  }

  @SneakyThrows
  private String generateAndUploadTicket(Subscribe subscribe) {
    File ticketFile = createTempFile("ticket-" + subscribe.getId(), ".json");
    objectMapper.writeValue(ticketFile, subscribe);

    String bucketKey = "tickets/" + subscribe.getId() + ".json";
    bucketComponent.upload(ticketFile, bucketKey);

    return bucketComponent.presign(bucketKey, Duration.ofDays(7)).toString();
  }
}
