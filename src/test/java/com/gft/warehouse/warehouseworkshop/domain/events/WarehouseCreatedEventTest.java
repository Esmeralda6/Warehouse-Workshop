package com.gft.warehouse.warehouseworkshop.domain.events;

import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseCreatedEventTest {

    private static final Location LOCATION = Location.builder().x(10).y(20).build();

    @Test
    void constructor_setsAllFields() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", LOCATION, "FACTORY");

        assertThat(event.getWarehouseId()).isEqualTo("wh-1");
        assertThat(event.getWarehouseName()).isEqualTo("Main Warehouse");
        assertThat(event.getLocation()).isEqualTo(LOCATION);
        assertThat(event.getWarehouseType()).isEqualTo("FACTORY");
    }

    @Test
    void getEventType_returnsWarehouseRegistered() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", LOCATION, "FACTORY");

        assertThat(event.getEventType()).isEqualTo("warehouse.registered.v1");
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", LOCATION, "FACTORY");
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isNotNull().isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        WarehouseCreatedEvent event1 = new WarehouseCreatedEvent("wh-1", "Main Warehouse", LOCATION, "FACTORY");
        WarehouseCreatedEvent event2 = new WarehouseCreatedEvent("wh-2", "Other Warehouse", LOCATION, "PRODUCTION");

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
}
