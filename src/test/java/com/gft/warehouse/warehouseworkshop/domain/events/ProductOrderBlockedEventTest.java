package com.gft.warehouse.warehouseworkshop.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ProductOrderBlockedEventTest {

    @Test
    void constructor_setsAllFields() {
        ProductOrderBlockedEvent event = new ProductOrderBlockedEvent(
                "product-1", "warehouse-1", ProductOrderBlockedEvent.DEFAULT_REASON, "7");

        assertThat(event.getProductId()).isEqualTo("product-1");
        assertThat(event.getWarehouseId()).isEqualTo("warehouse-1");
        assertThat(event.getReason()).isEqualTo(ProductOrderBlockedEvent.DEFAULT_REASON);
        assertThat(event.getBlockedSinceDay()).isEqualTo("7");
    }

    @Test
    void getEventType_returnsProductOrderBlocked() {
        ProductOrderBlockedEvent event = new ProductOrderBlockedEvent(
                "product-1", "warehouse-1", ProductOrderBlockedEvent.DEFAULT_REASON, "0");

        assertThat(event.getEventType()).isEqualTo("product.order.blocked.v1");
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        ProductOrderBlockedEvent event = new ProductOrderBlockedEvent(
                "product-1", "warehouse-1", ProductOrderBlockedEvent.DEFAULT_REASON, "3");
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        ProductOrderBlockedEvent e1 = new ProductOrderBlockedEvent("p-1", "w-1", "r", "1");
        ProductOrderBlockedEvent e2 = new ProductOrderBlockedEvent("p-2", "w-1", "r", "1");

        assertThat(e1.getEventId()).isNotEqualTo(e2.getEventId());
    }
}
