package com.subscribe.demo.subscribe.dto;

import java.util.UUID;

public record SubscribeRequest(UUID userId, UUID courseId) {
}
