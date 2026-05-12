package com.gft.warehouse.warehouseworkshop.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseCreatedEventTest {

    @Test
    void constructor_setsAllFields() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", "FACTORY");

        assertThat(event.getWarehouseId()).isEqualTo("wh-1");
        assertThat(event.getWarehouseName()).isEqualTo("Main Warehouse");
        assertThat(event.getWarehouseType()).isEqualTo("FACTORY");
    }

    @Test
    void getEventType_returnsWarehouseRegistered() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", "FACTORY");

        assertThat(event.getEventType()).isEqualTo("warehouse.registered.v1");
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", "FACTORY");
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isNotNull().isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        WarehouseCreatedEvent event1 = new WarehouseCreatedEvent("wh-1", "Main Warehouse", "FACTORY");
        WarehouseCreatedEvent event2 = new WarehouseCreatedEvent("wh-2", "Other Warehouse", "PRODUCTION");

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
}
