package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class WarehouseEventListenerTest {

    private final WarehouseEventListener listener = new WarehouseEventListener();

    @Test
    void onWarehouseRegistered_doesNotThrow() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", Location.builder().x(1).y(1).build(), "FACTORY");

        assertThatCode(() -> listener.onWarehouseRegistered(event)).doesNotThrowAnyException();
    }
}
