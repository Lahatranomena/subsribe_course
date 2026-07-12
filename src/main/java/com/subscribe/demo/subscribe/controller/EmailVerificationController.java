package com.subscribe.demo.subscribe.controller;

import com.subscribe.demo.mail.EmailAddressVerifier;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class EmailVerificationController {

  private final EmailAddressVerifier emailAddressVerifier;

  @PostMapping("/verify-email")
  public String verify(@RequestParam String email) throws AddressException {
    emailAddressVerifier.accept(new InternetAddress(email));
    return "Email de vérification envoyé à " + email;
  }
}
