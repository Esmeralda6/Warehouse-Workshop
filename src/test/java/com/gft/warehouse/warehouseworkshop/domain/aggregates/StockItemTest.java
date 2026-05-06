package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class StockItemTest {

    @Test
    public void generateStockItem(){
        UUID uuid = UUID.randomUUID();
        StockItem item = StockItem.builder()
                .productId( ProductId.builder().id(uuid).build())
                .quantity(Quantity.builder().quantity(10).build())
                .build();

        assertThat( item ).isInstanceOf( StockItem.class ).isNotNull();
        assertThat( item.getProductId() ).isInstanceOf( ProductId.class ).isNotNull();
        assertThat( item.getQuantity() ).isInstanceOf( Quantity.class) .isNotNull();
    }
}