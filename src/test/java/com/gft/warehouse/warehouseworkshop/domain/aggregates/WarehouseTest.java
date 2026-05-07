package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.exceptions.InsuficientStockException;
import com.gft.warehouse.warehouseworkshop.domain.services.ReplenishmentPolicy;
import com.gft.warehouse.warehouseworkshop.domain.services.StockChecker;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WarehouseTest {

    private StockItem buildItem(UUID uuid, int qty) {
        return StockItem.builder()
                .productId(ProductId.builder().id(uuid).build())
                .quantity(Quantity.builder().quantity(qty).build())
                .build();
    }

    private Warehouse buildWarehouse(boolean isStockInfinite, List<StockItem> stock) {
        return Warehouse.builder()
                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                .warehouseName("Warehouse_1")
                .warehouseType(Type.PRODUCTION)
                .warehouseLocation(Location.builder().x(1).y(1).build())
                .minimumStockRules(Map.of(1, 100))
                .isStockInfinite(isStockInfinite)
                .stock(stock)
                .build();
    }

    private static Stream<Arguments> providerGeneratedWarehouse() {
        return Stream.of(
                Arguments.of(Warehouse.builder()
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .warehouseName("Warehouse_1")
                        .warehouseType(Type.PRODUCTION)
                        .warehouseLocation(Location.builder().x(1).y(1).build())
                        .minimumStockRules(Map.of(1, 100))
                        .isStockInfinite(true)
                        .factoryId(FactoryId.builder().id(UUID.randomUUID()).build())
                        .build()),
                Arguments.of(Warehouse.builder()
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .warehouseName("Warehouse_1")
                        .warehouseType(Type.PRODUCTION)
                        .warehouseLocation(Location.builder().x(1).y(1).build())
                        .minimumStockRules(Map.of(1, 100))
                        .isStockInfinite(true)
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("providerGeneratedWarehouse")
    void generateWarehouse(Warehouse warehouse) {
        assertThat(warehouse).isInstanceOf(Warehouse.class);
        assertThat(warehouse.getWarehouseId()).isInstanceOf(WarehouseId.class).isNotNull();
        assertThat(warehouse.getWarehouseName()).isInstanceOf(String.class).isNotNull();
        assertThat(warehouse.getWarehouseType()).isInstanceOf(Type.class).isNotNull();
        assertThat(warehouse.getWarehouseLocation()).isInstanceOf(Location.class).isNotNull();
        assertThat(warehouse.getMinimumStockRules()).isInstanceOf(Map.class).isNotNull();
        assertThat(warehouse.isStockInfinite()).isInstanceOf(Boolean.class).isNotNull();
    }

    @Test
    void checkOwnStock_returnsTrueWhenCheckerReturnsTrue() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>(List.of(buildItem(productId, 10))));
        StockChecker checker = (stock, items) -> true;

        assertThat(warehouse.checkOwnStock(List.of(buildItem(productId, 5)), checker)).isTrue();
    }

    @Test
    void checkOwnStock_returnsFalseWhenCheckerReturnsFalse() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>(List.of(buildItem(productId, 3))));
        StockChecker checker = (stock, items) -> false;

        assertThat(warehouse.checkOwnStock(List.of(buildItem(productId, 5)), checker)).isFalse();
    }

    @Test
    void consumeStock_reducesQuantityForMatchingItem() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>(List.of(buildItem(productId, 10))));

        warehouse.consumeStock(List.of(buildItem(productId, 4)));

        assertThat(warehouse.getStock().get(0).getQuantity().getQuantity()).isEqualTo(6);
    }

    @Test
    void consumeStock_throwsWhenNotEnoughStock() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>(List.of(buildItem(productId, 2))));

        assertThrows(InsuficientStockException.class,
                () -> warehouse.consumeStock(List.of(buildItem(productId, 5))));
    }

    @Test
    void receiveDelivery_increasesQuantityForExistingProduct() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>(List.of(buildItem(productId, 5))));

        warehouse.receiveDelivery(List.of(buildItem(productId, 3)));

        assertThat(warehouse.getStock().get(0).getQuantity().getQuantity()).isEqualTo(8);
    }

    @Test
    void receiveDelivery_addsNewItemForUnknownProduct() {
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>());
        UUID newProduct = UUID.randomUUID();

        warehouse.receiveDelivery(List.of(buildItem(newProduct, 10)));

        assertThat(warehouse.getStock()).hasSize(1);
        assertThat(warehouse.getStock().get(0).getQuantity().getQuantity()).isEqualTo(10);
    }

    @Test
    void needsReplenishment_returnsFalseWhenStockIsInfiniteRegardlessOfPolicy() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(true, new ArrayList<>(List.of(buildItem(productId, 0))));
        ReplenishmentPolicy alwaysReplenish = (stock, rules) -> true;

        assertThat(warehouse.needsReplenishment(alwaysReplenish)).isFalse();
    }

    @Test
    void needsReplenishment_returnsTrueWhenPolicySaysReplenish() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>(List.of(buildItem(productId, 0))));
        ReplenishmentPolicy alwaysReplenish = (stock, rules) -> true;

        assertThat(warehouse.needsReplenishment(alwaysReplenish)).isTrue();
    }

    @Test
    void needsReplenishment_returnsFalseWhenPolicySaysNoReplenishment() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>(List.of(buildItem(productId, 5))));
        ReplenishmentPolicy neverReplenish = (stock, rules) -> false;

        assertThat(warehouse.needsReplenishment(neverReplenish)).isFalse();
    }

    @Test
    void dispatchItems_reducesStockLikeConsumeStock() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(false, new ArrayList<>(List.of(buildItem(productId, 10))));

        warehouse.dispatchItems(List.of(buildItem(productId, 3)));

        assertThat(warehouse.getStock().get(0).getQuantity().getQuantity()).isEqualTo(7);
    }
}