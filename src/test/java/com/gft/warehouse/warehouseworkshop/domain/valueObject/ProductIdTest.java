package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class ProductIdTest {
    @Test
    void generateProductId(){
        UUID uuid = UUID.randomUUID();
        ProductId productId = ProductId.builder()
                .id(uuid)
                .build();

        assertThat(productId).isInstanceOf(ProductId.class).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenIsNull(){
        assertThatThrownBy(() -> ProductId.builder().id(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The ID of the product can't be empty.");
    }

}