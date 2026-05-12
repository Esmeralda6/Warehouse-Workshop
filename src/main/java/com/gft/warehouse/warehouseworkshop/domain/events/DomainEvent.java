package com.gft.warehouse.warehouseworkshop.domain.events;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {

    private final String eventId;
    private final Instant occurredOn;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }

    public abstract String getEventType();
}
