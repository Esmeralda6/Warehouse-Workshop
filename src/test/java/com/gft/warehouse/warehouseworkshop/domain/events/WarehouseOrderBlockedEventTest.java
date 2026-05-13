package com.gft.warehouse.warehouseworkshop.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseOrderBlockedEventTest {

    @Test
    void constructor_setsAllFields() {
        WarehouseOrderBlockedEvent event = new WarehouseOrderBlockedEvent("order-1");

        assertThat(event.getOrderId()).isEqualTo("order-1");
    }

    @Test
    void getEventType_returnsWarehouseOrderBlocked() {
        WarehouseOrderBlockedEvent event = new WarehouseOrderBlockedEvent("order-1");

        assertThat(event.getEventType()).isEqualTo("warehouse.order.blocked.v1");
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        WarehouseOrderBlockedEvent event = new WarehouseOrderBlockedEvent("order-1");
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isNotNull().isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        WarehouseOrderBlockedEvent event1 = new WarehouseOrderBlockedEvent("order-1");
        WarehouseOrderBlockedEvent event2 = new WarehouseOrderBlockedEvent("order-2");

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
}
