package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.ProductionOrderCompletedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class ProductionOrderCompletedListenerTest {

    private final ProductionOrderCompletedListener listener = new ProductionOrderCompletedListener();

    @Test
    void onProductionOrderCompleted_doesNotThrow() {
        ProductionOrderCompletedEvent event = new ProductionOrderCompletedEvent();
        event.setProductionOrderId("po-1");
        event.setWarehouseOrderId("wo-1");
        event.setProductId("prod-1");
        event.setFactoryAsign("factory-1");
        event.setQuantity(10);
        event.setStatus("COMPLETED");

        assertThatCode(() -> listener.onProductionOrderCompleted(event)).doesNotThrowAnyException();
    }
}
