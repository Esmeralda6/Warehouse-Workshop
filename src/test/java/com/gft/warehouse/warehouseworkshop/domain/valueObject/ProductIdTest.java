package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ProductIdTest {
    @Test
    void generateProductId(){
        ProductId productId = ProductId.builder()
                .id("id_1")
                .build();

        assertThat(productId).isInstanceOf(ProductId.class).isNotNull();
        assertThat(productId.getId()).isNotBlank();
    }

    @Test
    void shouldThrowExceptionWhenIsEmpty(){
        assertThatThrownBy(() -> ProductId.builder().id("").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The ID of the product can't be blank.");

    }

    @Test
    void shouldThrowExceptionWhenIsNull(){
        assertThatThrownBy(() -> ProductId.builder().id(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The ID of the product can't be empty.");
    }

}