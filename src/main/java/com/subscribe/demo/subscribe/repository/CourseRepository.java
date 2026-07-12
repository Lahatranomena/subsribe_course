package com.subscribe.demo.subscribe.repository;

import com.subscribe.demo.subscribe.entity.Course;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, UUID> {}
