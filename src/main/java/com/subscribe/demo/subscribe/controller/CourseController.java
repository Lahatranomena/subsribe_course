package com.subscribe.demo.subscribe.controller;

import com.subscribe.demo.subscribe.entity.Course;
import com.subscribe.demo.subscribe.repository.CourseRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {
  private CourseRepository courseRepository;

  @PostMapping
  public Course create(@RequestBody Course course) {
    return courseRepository.save(course);
  }

  @GetMapping("/{id}")
  public Course get(@PathVariable UUID id) {
    return courseRepository.findById(id).orElseThrow();
  }
}
