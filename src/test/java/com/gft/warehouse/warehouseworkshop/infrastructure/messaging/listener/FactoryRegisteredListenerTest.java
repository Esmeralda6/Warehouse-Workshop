package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.FactoryRegisteredEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class FactoryRegisteredListenerTest {

    private final FactoryRegisteredListener listener = new FactoryRegisteredListener();

    @Test
    void onFactoryRegistered_doesNotThrow() {
        FactoryRegisteredEvent event = new FactoryRegisteredEvent();
        event.setFactoryId("factory-1");
        event.setWarehouseId("warehouse-1");

        assertThatCode(() -> listener.onFactoryRegistered(event)).doesNotThrowAnyException();
    }
}
