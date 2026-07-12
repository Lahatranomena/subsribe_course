package com.subscribe.demo.subscribe.controller;

import com.subscribe.demo.subscribe.entity.User;
import com.subscribe.demo.subscribe.repository.UserRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
  private final UserRepository userRepository;

  @PostMapping
  public User create(@RequestBody User user) {
    return userRepository.save(user);
  }

  @GetMapping("/{id}")
  public User get(@PathVariable UUID id) {
    return userRepository.findById(id).orElseThrow();
  }
}
