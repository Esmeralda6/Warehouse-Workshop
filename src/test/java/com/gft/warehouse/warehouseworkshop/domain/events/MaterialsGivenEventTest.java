package com.gft.warehouse.warehouseworkshop.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MaterialsGivenEventTest {

    @Test
    void constructor_setsItems() {
        List<MaterialsGivenEvent.Item> items = List.of(new MaterialsGivenEvent.Item("prod-1", 5));
        MaterialsGivenEvent event = new MaterialsGivenEvent(items);

        assertThat(event.getItems()).hasSize(1);
    }

    @Test
    void getEventType_returnsMaterialsGiven() {
        MaterialsGivenEvent event = new MaterialsGivenEvent(List.of());

        assertThat(event.getEventType()).isEqualTo("materials.given.v1");
    }

    @Test
    void item_setsAllFields() {
        MaterialsGivenEvent.Item item = new MaterialsGivenEvent.Item("prod-1", 8);

        assertThat(item.getProductId()).isEqualTo("prod-1");
        assertThat(item.getQuantity()).isEqualTo(8);
    }

    @Test
    void domainEventFields_arePopulatedOnCreation() {
        Instant before = Instant.now();
        MaterialsGivenEvent event = new MaterialsGivenEvent(List.of());
        Instant after = Instant.now();

        assertThat(event.getEventId()).isNotNull().isNotBlank();
        assertThat(event.getOccurredOn()).isNotNull().isBetween(before, after);
    }

    @Test
    void twoEvents_haveDistinctEventIds() {
        MaterialsGivenEvent event1 = new MaterialsGivenEvent(List.of());
        MaterialsGivenEvent event2 = new MaterialsGivenEvent(List.of());

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
}
