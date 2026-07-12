package com.subscribe.demo.endpoint.event.model;

import lombok.*;

import java.time.Duration;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class SubscriptionConfirmedRequest extends PojaEvent{
    private UUID id;

    @Override
    public Duration maxConsumerDuration() {
        return Duration.ofSeconds(45);
    }

    @Override
    public Duration maxConsumerBackoffBetweenRetries() {
        return Duration.ofSeconds(30);
    }
}
