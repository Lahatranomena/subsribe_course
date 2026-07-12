package com.subscribe.demo.subscribe.repository;

import com.subscribe.demo.subscribe.entity.Subscribe;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscribe, UUID> {}
