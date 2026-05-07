package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;


class QuantityTest {

    @Test
    void generateQuantity(){
        Quantity quantity = Quantity.builder()
                .value(10)
                .build();

        assertThat(quantity.getValue()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        assertThatThrownBy(() -> Quantity.builder().value(-1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The quantity can't be negative.");
    }
}


