package com.subscribe.demo.service.event;

import static java.io.File.createTempFile;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.subscribe.demo.endpoint.event.model.SubscriptionConfirmedRequest;
import com.subscribe.demo.file.bucket.BucketComponent;
import com.subscribe.demo.mail.Email;
import com.subscribe.demo.mail.Mailer;
import com.subscribe.demo.subscribe.entity.Subscribe;
import com.subscribe.demo.subscribe.repository.SubscriptionRepository;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileOutputStream;
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
            + "\">Télécharger votre ticket d'inscription (PDF)</a></p>";

    mailer.accept(
        new Email(recipient, List.of(), List.of(), "Inscription confirmée", body, List.of()));
  }

  @SneakyThrows
  private String generateAndUploadTicket(Subscribe subscribe) {
    File ticketFile = createTempFile("ticket-" + subscribe.getId(), ".pdf");

    Document document = new Document();

    PdfWriter.getInstance(document, new FileOutputStream(ticketFile));

    document.open();

    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

    document.add(new Paragraph("Ticket d'inscription", titleFont));
    document.add(new Paragraph(" "));
    document.add(
        new Paragraph(
            "Participant : "
                + subscribe.getUser().getFirstname()
                + " "
                + subscribe.getUser().getLastname(),
            bodyFont));
    document.add(new Paragraph("Cours : " + subscribe.getCourse().getTitle(), bodyFont));
    document.add(new Paragraph("Du : " + subscribe.getCourse().getStartDate(), bodyFont));
    document.add(new Paragraph("Au : " + subscribe.getCourse().getEndDate(), bodyFont));
    document.add(new Paragraph("Référence inscription : " + subscribe.getId(), bodyFont));

    document.close();

    String bucketKey = "tickets/" + subscribe.getId() + ".pdf";
    bucketComponent.upload(ticketFile, bucketKey);

    return bucketComponent.presign(bucketKey, Duration.ofDays(7)).toString();
  }
}
