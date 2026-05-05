package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WarehouseIdTest {

    @Test
    void shouldCreateWarehouseIdWithValidId() {
        WarehouseId warehouseId = WarehouseId.builder().id("warehouse-001").build();

        assertThat(warehouseId).isNotNull().isInstanceOf(WarehouseId.class);
        assertThat(warehouseId.getId()).isEqualTo("warehouse-001");
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThatThrownBy(() -> WarehouseId.builder().id(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("WarehouseId cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenIdIsBlank() {
        assertThatThrownBy(() -> WarehouseId.builder().id("   ").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("WarehouseId cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenIdIsEmpty() {
        assertThatThrownBy(() -> WarehouseId.builder().id("").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("WarehouseId cannot be null or blank");
    }
}