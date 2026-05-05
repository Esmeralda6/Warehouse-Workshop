package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class QuantityTest {
    @Test
    void generateQuantity(){
        Quantity quantity = Quantity.builder()
                .quantity(10)
                .build();

        assertThat(quantity.getQuantity()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        assertThatThrownBy(() -> Quantity.builder().quantity(-1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The quantity can't be negative.");
    }
}


