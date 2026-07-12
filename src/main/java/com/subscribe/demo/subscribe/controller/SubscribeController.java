package com.subscribe.demo.subscribe.controller;

import com.subscribe.demo.endpoint.event.EventProducer;
import com.subscribe.demo.endpoint.event.model.SubscriptionConfirmedRequest;
import com.subscribe.demo.subscribe.dto.SubscribeRequest;
import com.subscribe.demo.subscribe.entity.Course;
import com.subscribe.demo.subscribe.entity.Subscribe;
import com.subscribe.demo.subscribe.entity.User;
import com.subscribe.demo.subscribe.repository.CourseRepository;
import com.subscribe.demo.subscribe.repository.SubscriptionRepository;
import com.subscribe.demo.subscribe.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribe")
@AllArgsConstructor
public class SubscribeController {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final SubscriptionRepository subscribeRepository;
  private final EventProducer<SubscriptionConfirmedRequest> eventProducer;

  @PostMapping
  public Subscribe subscribe(@RequestBody SubscribeRequest request) {
    User user =
        userRepository
            .findById(request.userId())
            .orElseThrow(
                () -> new IllegalArgumentException("User introuvable: " + request.userId()));
    Course course =
        courseRepository
            .findById(request.courseId())
            .orElseThrow(
                () -> new IllegalArgumentException("Course introuvable: " + request.courseId()));

    Subscribe subscribe =
        subscribeRepository.save(Subscribe.builder().user(user).course(course).build());

    eventProducer.accept(
        List.of(SubscriptionConfirmedRequest.builder().subscribeId(subscribe.getId()).build()));

    return subscribe;
  }
}
