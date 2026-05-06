package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FactoryIdTest {

    @Test
    void shouldCreateFactoryIdWithValidId() {
        FactoryId factoryId = FactoryId.builder().id(UUID.randomUUID()).build();

        assertThat(factoryId).isNotNull().isInstanceOf(FactoryId.class);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThatThrownBy(() -> FactoryId.builder().id(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("FactoryId cannot be null");
    }
}