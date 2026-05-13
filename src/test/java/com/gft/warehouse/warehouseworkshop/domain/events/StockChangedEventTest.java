package com.gft.warehouse.warehouseworkshop.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class StockChangedEventTest {

    @Test
    void constructor_setsAllFields() {
        StockChangedEvent event = new StockChangedEvent("prod-1", 20, "WOOD");

        assertThat(event.getProductId()).isEqualTo("prod-1");
        assertThat(event.getQuantity()).isEqualTo(20);
        assertThat(event.getType()).isEqualTo("WOOD");
    }

    @Test
    void getEventType_returnsWarehouseStockChanged() {
        StockChangedEvent event = new StockChangedEvent("prod-1", 20, "WOOD");

        assertThat(event.getEventType()).isEqualTo("warehouse.stock.changed.v1");
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        StockChangedEvent event = new StockChangedEvent("prod-1", 20, "WOOD");
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isNotNull().isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        StockChangedEvent event1 = new StockChangedEvent("prod-1", 5, "METAL");
        StockChangedEvent event2 = new StockChangedEvent("prod-2", 3, "WOOD");

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
}
