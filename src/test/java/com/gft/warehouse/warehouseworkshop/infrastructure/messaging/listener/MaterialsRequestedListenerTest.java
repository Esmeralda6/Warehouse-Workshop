package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.MaterialsRequestedEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;

class MaterialsRequestedListenerTest {

    private final MaterialsRequestedListener listener = new MaterialsRequestedListener();

    @Test
    void onMaterialsRequested_doesNotThrow() {
        MaterialsRequestedEvent event = new MaterialsRequestedEvent();
        event.setItems(List.of());

        assertThatCode(() -> listener.onMaterialsRequested(event)).doesNotThrowAnyException();
    }
}
