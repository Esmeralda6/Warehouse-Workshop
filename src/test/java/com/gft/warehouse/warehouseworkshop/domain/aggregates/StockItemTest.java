package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class StockItemTest {

    private StockItem buildItem(UUID uuid, int qty) {
        return StockItem.builder()
                .productId(ProductId.builder().id(uuid).build())
                .quantity(Quantity.builder().quantity(qty).build())
                .build();
    }

    @Test
    void generateStockItem() {
        UUID uuid = UUID.randomUUID();
        StockItem item = buildItem(uuid, 10);

        assertThat(item).isInstanceOf(StockItem.class).isNotNull();
        assertThat(item.getProductId()).isInstanceOf(ProductId.class).isNotNull();
        assertThat(item.getQuantity()).isInstanceOf(Quantity.class).isNotNull();
    }

    @Test
    void isEnough_returnsTrueWhenStockIsSufficient() {
        StockItem item = buildItem(UUID.randomUUID(), 10);
        assertThat(item.isEnough(Quantity.builder().quantity(5).build())).isTrue();
    }

    @Test
    void isEnough_returnsFalseWhenStockIsInsufficient() {
        StockItem item = buildItem(UUID.randomUUID(), 3);
        assertThat(item.isEnough(Quantity.builder().quantity(5).build())).isFalse();
    }

    @Test
    void add_increasesQuantity() {
        StockItem item = buildItem(UUID.randomUUID(), 5);
        item.add(Quantity.builder().quantity(3).build());
        assertThat(item.getQuantity().getQuantity()).isEqualTo(8);
    }

    @Test
    void subtract_decreasesQuantity() {
        StockItem item = buildItem(UUID.randomUUID(), 10);
        item.subtract(Quantity.builder().quantity(4).build());
        assertThat(item.getQuantity().getQuantity()).isEqualTo(6);
    }

    @Test
    void subtract_throwsWhenNotEnoughStock() {
        StockItem item = buildItem(UUID.randomUUID(), 2);
        assertThrows(IllegalArgumentException.class,
                () -> item.subtract(Quantity.builder().quantity(5).build()));
    }

    @Test
    void hasProduct_returnsTrueForMatchingProduct() {
        UUID uuid = UUID.randomUUID();
        StockItem item = buildItem(uuid, 5);
        assertThat(item.hasProduct(ProductId.builder().id(uuid).build())).isTrue();
    }

    @Test
    void hasProduct_returnsFalseForDifferentProduct() {
        StockItem item = buildItem(UUID.randomUUID(), 5);
        assertThat(item.hasProduct(ProductId.builder().id(UUID.randomUUID()).build())).isFalse();
    }
}
