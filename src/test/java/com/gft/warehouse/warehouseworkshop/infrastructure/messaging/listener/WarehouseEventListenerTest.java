package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class WarehouseEventListenerTest {

    private final WarehouseEventListener listener = new WarehouseEventListener();

    @Test
    void onWarehouseCreated_doesNotThrow() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", "FACTORY");

        assertThatCode(() -> listener.onWarehouseCreated(event)).doesNotThrowAnyException();
    }
}
