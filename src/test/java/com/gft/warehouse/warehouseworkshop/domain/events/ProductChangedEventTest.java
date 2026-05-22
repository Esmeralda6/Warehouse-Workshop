package com.gft.warehouse.warehouseworkshop.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ProductChangedEventTest {

    @Test
    void constructor_setsAllFields() {
        ProductChangedEvent event = new ProductChangedEvent("prod-1", "Wheel");

        assertThat(event.getProduct().getProductId()).isEqualTo("prod-1");
        assertThat(event.getProduct().getProdutName()).isEqualTo("Wheel");
    }

    @Test
    void getEventType_returnsWarehouseProductChanged() {
        ProductChangedEvent event = new ProductChangedEvent("prod-1", "Wheel");

        assertThat(event.getEventType()).isEqualTo("warehouse.product.changed.v1");
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        ProductChangedEvent event = new ProductChangedEvent("prod-1", "Wheel");
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isNotNull().isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        ProductChangedEvent event1 = new ProductChangedEvent("prod-1", "Wheel");
        ProductChangedEvent event2 = new ProductChangedEvent("prod-2", "Axle");

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
}