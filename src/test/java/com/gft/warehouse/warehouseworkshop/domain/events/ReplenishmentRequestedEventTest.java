package com.gft.warehouse.warehouseworkshop.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ReplenishmentRequestedEventTest {

    @Test
    void constructor_setsAllFields() {
        ReplenishmentRequestedEvent event = new ReplenishmentRequestedEvent("prod-1", 10, "WOOD");

        assertThat(event.getProductId()).isEqualTo("prod-1");
        assertThat(event.getQuantity()).isEqualTo(10);
        assertThat(event.getType()).isEqualTo("WOOD");
    }

    @Test
    void getEventType_returnsReplenishmentRequested() {
        ReplenishmentRequestedEvent event = new ReplenishmentRequestedEvent("prod-1", 10, "WOOD");

        assertThat(event.getEventType()).isEqualTo("replenishment.requested.v1");
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        ReplenishmentRequestedEvent event = new ReplenishmentRequestedEvent("prod-1", 10, "WOOD");
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isNotNull().isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        ReplenishmentRequestedEvent event1 = new ReplenishmentRequestedEvent("prod-1", 5, "METAL");
        ReplenishmentRequestedEvent event2 = new ReplenishmentRequestedEvent("prod-2", 3, "WOOD");

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
}
