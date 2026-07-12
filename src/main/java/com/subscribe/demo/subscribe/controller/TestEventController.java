package com.subscribe.demo.subscribe.controller;

import com.subscribe.demo.endpoint.event.EventProducer;
import com.subscribe.demo.endpoint.event.model.TestEventTriggered;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TestEventController {

  private final EventProducer<TestEventTriggered> eventProducer;

  @PostMapping("/test-event")
  public String test(@RequestParam String to) {
    eventProducer.accept(List.of(TestEventTriggered.builder().to(to).build()));
    return "Event envoyé";
  }
}
