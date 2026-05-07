package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WarehouseIdTest {

    @Test
    void shouldCreateWarehouseIdWithValidId() {
        UUID uuid = UUID.randomUUID();
        WarehouseId warehouseId = WarehouseId.builder().id(uuid).build();

        assertThat(warehouseId).isNotNull().isInstanceOf(WarehouseId.class);
        assertThat(warehouseId.getId()).isEqualTo(uuid);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThatThrownBy(() -> WarehouseId.builder().id(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("WarehouseId cannot be null");
    }
}