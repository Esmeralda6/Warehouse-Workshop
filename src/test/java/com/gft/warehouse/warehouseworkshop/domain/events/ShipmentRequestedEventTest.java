package com.gft.warehouse.warehouseworkshop.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentRequestedEventTest {

    @Test
    void constructor_setsAllFields() {
        List<ShipmentRequestedEvent.Item> items = List.of(new ShipmentRequestedEvent.Item("wood", 6));
        ShipmentRequestedEvent event = new ShipmentRequestedEvent("ship-1", "wh-north", "wh-south", items, 3);

        assertThat(event.getShipmentId()).isEqualTo("ship-1");
        assertThat(event.getOriginId()).isEqualTo("wh-north");
        assertThat(event.getDestinationId()).isEqualTo("wh-south");
        assertThat(event.getRequestedAt()).isEqualTo(3);
        assertThat(event.getItems()).hasSize(1);
    }

    @Test
    void getEventType_returnsShipmentRequested() {
        ShipmentRequestedEvent event = new ShipmentRequestedEvent("ship-1", "wh-north", "wh-south", List.of(), 3);

        assertThat(event.getEventType()).isEqualTo("shipment.requested.v1");
    }

    @Test
    void item_setsAllFields() {
        ShipmentRequestedEvent.Item item = new ShipmentRequestedEvent.Item("metal", 4);

        assertThat(item.getMaterialType()).isEqualTo("metal");
        assertThat(item.getQuantity()).isEqualTo(4);
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        ShipmentRequestedEvent event = new ShipmentRequestedEvent("ship-1", "wh-north", "wh-south", List.of(), 1);
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isNotNull().isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        ShipmentRequestedEvent event1 = new ShipmentRequestedEvent("ship-1", "wh-a", "wh-b", List.of(), 1);
        ShipmentRequestedEvent event2 = new ShipmentRequestedEvent("ship-2", "wh-c", "wh-d", List.of(), 2);

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
}
