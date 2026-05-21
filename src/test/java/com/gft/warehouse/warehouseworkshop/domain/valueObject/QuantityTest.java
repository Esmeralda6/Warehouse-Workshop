package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class QuantityTest {

    @ParameterizedTest(name = "value={0} is valid")
    @ValueSource(ints = {0, 1, 10, 100})
    void shouldCreateQuantityWithValidValue(int value) {
        Quantity quantity = Quantity.builder().value(value).build();

        assertThat(quantity.getValue()).isEqualTo(value);
    }

    @ParameterizedTest(name = "value={0} throws exception")
    @ValueSource(ints = {-1, -10, Integer.MIN_VALUE})
    void shouldThrowExceptionWhenValueIsNegative(int value) {
        assertThatThrownBy(() -> Quantity.builder().value(value).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The quantity can't be negative.");
    }
}