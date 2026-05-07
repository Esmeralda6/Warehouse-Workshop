package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockItemIdTest {
    @Test
    void generateStockItemId(){
        UUID uuid = UUID.randomUUID();
        StockItemId stockItemId = StockItemId.builder()
                .id(uuid)
                .build();

        assertThat(stockItemId).isInstanceOf(StockItemId.class).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenIsNull(){
        assertThatThrownBy(() -> StockItemId.builder().id(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The ID of the stock item can't be empty.");
    }

}