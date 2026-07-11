package com.subscribe.demo.endpoint.event.consumer.model;

import com.subscribe.demo.PojaGenerated;
import com.subscribe.demo.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
