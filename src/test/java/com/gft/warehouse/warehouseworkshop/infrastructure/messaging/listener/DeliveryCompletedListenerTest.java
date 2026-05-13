package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.DeliveryCompletedEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;

class DeliveryCompletedListenerTest {

    private final DeliveryCompletedListener listener = new DeliveryCompletedListener();

    @Test
    void onDeliveryCompleted_doesNotThrow() {
        DeliveryCompletedEvent event = new DeliveryCompletedEvent();
        event.setShipmentId("ship-1");
        event.setTruckId("truck-1");
        event.setCompletedAt(5);
        event.setItems(List.of());

        assertThatCode(() -> listener.onDeliveryCompleted(event)).doesNotThrowAnyException();
    }
}
