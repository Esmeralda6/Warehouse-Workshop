package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.MaterialsRequestedEvent;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;

class MaterialsRequestedListenerTest {

    private final MaterialsRequestedListener listener = new MaterialsRequestedListener();

    private static Stream<MaterialsRequestedEvent> eventProvider() {
        MaterialsRequestedEvent withItems = new MaterialsRequestedEvent();
        withItems.setItems(List.of());

        MaterialsRequestedEvent withNullItems = new MaterialsRequestedEvent();
        withNullItems.setItems(null);

        return Stream.of(withItems, withNullItems);
    }

    @ParameterizedTest
    @MethodSource("eventProvider")
    void onMaterialsRequested_doesNotThrow(MaterialsRequestedEvent event) {
        assertThatCode(() -> listener.onMaterialsRequested(event)).doesNotThrowAnyException();
    }
}
