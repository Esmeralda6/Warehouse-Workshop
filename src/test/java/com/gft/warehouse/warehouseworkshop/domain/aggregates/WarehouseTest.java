package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;
import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.exceptions.InsuficientStockException;
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
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WarehouseTest {

    private StockItem buildItem(UUID uuid, int qty) {
        return StockItem.builder()
                .productId(ProductId.builder().id(uuid).build())
                .quantity(Quantity.builder().value(qty).build())
                .build();
    }

    private Warehouse buildWarehouse(List<StockItem> stock) {
        return Warehouse.builder()
                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                .warehouseName("Warehouse_1")
                .warehouseType(WarehouseType.PRODUCTION)
                .warehouseLocation(Location.builder().x(1).y(1).build())
                .stock(stock)
                .build();
    }

    private static Stream<Arguments> providerGeneratedWarehouse() {
        return Stream.of(
                Arguments.of(Warehouse.builder()
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .warehouseName("Warehouse_1")
                        .warehouseType( WarehouseType.PRODUCTION )
                        .warehouseLocation( Location.builder().x(1).y(1).build())
                        .factoryId(FactoryId.builder().id(UUID.randomUUID()).build())
                        .build()),
                Arguments.of(Warehouse.builder()
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .warehouseName("Warehouse_1")
                        .warehouseType( WarehouseType.PRODUCTION )
                        .warehouseLocation( Location.builder().x(1).y(1).build())
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("providerGeneratedWarehouse")
    void generateWarehouse(Warehouse warehouse) {
        assertThat(warehouse).isInstanceOf(Warehouse.class);
        assertThat(warehouse.getWarehouseId()).isInstanceOf(WarehouseId.class).isNotNull();
        assertThat(warehouse.getWarehouseName()).isInstanceOf(String.class).isNotNull();
        assertThat(warehouse.getWarehouseType()).isInstanceOf(WarehouseType.class).isNotNull();
        assertThat(warehouse.getWarehouseLocation()).isInstanceOf(Location.class).isNotNull();
    }

    private static Stream<Arguments> checkOwnStockCases() {
        UUID productId = UUID.randomUUID();
        return Stream.of(
                Arguments.of(
                        new ArrayList<>(List.of(StockItem.builder()
                                .productId(ProductId.builder().id(productId).build())
                                .quantity(Quantity.builder().value(10).build()).build())),
                        List.of(StockItem.builder()
                                .productId(ProductId.builder().id(productId).build())
                                .quantity(Quantity.builder().value(5).build()).build()),
                        (StockChecker) (stock, items) -> true,
                        true
                ),
                Arguments.of(
                        new ArrayList<>(List.of(StockItem.builder()
                                .productId(ProductId.builder().id(productId).build())
                                .quantity(Quantity.builder().value(3).build()).build())),
                        List.of(StockItem.builder()
                                .productId(ProductId.builder().id(productId).build())
                                .quantity(Quantity.builder().value(5).build()).build()),
                        (StockChecker) (stock, items) -> false,
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource("checkOwnStockCases")
    void checkOwnStock(List<StockItem> stock, List<StockItem> requested, StockChecker checker, boolean expected) {
        Warehouse warehouse = buildWarehouse(stock);
        assertThat(warehouse.checkOwnStock(requested, checker)).isEqualTo(expected);
    }

    @Test
    void consumeStock_reducesQuantityAndReturnsConsumedItems() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(new ArrayList<>(List.of(buildItem(productId, 10))));
        List<StockItem> requested = List.of(buildItem(productId, 4));

        List<StockItem> consumed = warehouse.consumeStock(requested);

        assertThat(warehouse.getStock().getFirst().getQuantity().getValue()).isEqualTo(6);
        assertThat(consumed).isEqualTo(requested);
        assertThat(consumed.getFirst().getQuantity().getValue()).isEqualTo(4);
    }

    @Test
    void consumeStock_throwsWhenNotEnoughStock() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(new ArrayList<>(List.of(buildItem(productId, 2))));

        assertThrows(InsuficientStockException.class,
                () -> warehouse.consumeStock(List.of(buildItem(productId, 5))));
    }

    @Test
    void receiveDelivery_increasesQuantityForExistingProduct() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(new ArrayList<>(List.of(buildItem(productId, 5))));

        warehouse.receiveDelivery(List.of(buildItem(productId, 3)));

        assertThat(warehouse.getStock().getFirst().getQuantity().getValue()).isEqualTo(8);
    }

    @Test
    void receiveDelivery_addsNewItemForUnknownProduct() {
        Warehouse warehouse = buildWarehouse(new ArrayList<>());
        UUID newProduct = UUID.randomUUID();

        warehouse.receiveDelivery(List.of(buildItem(newProduct, 10)));

        assertThat(warehouse.getStock()).hasSize(1);
        assertThat(warehouse.getStock().getFirst().getQuantity().getValue()).isEqualTo(10);
    }

    @Test
    void dispatchItems_reducesStockAndReturnsDispatchedItems() {
        UUID productId = UUID.randomUUID();
        Warehouse warehouse = buildWarehouse(new ArrayList<>(List.of(buildItem(productId, 10))));
        List<StockItem> requested = List.of(buildItem(productId, 3));

        List<StockItem> dispatched = warehouse.dispatchItems(requested);

        assertThat(warehouse.getStock().getFirst().getQuantity().getValue()).isEqualTo(7);
        assertThat(dispatched).isEqualTo(requested);
        assertThat(dispatched.getFirst().getQuantity().getValue()).isEqualTo(3);
    }

    @Test
    void recordEvent_addsDomainEventToList() {
        Warehouse warehouse = buildWarehouse(new ArrayList<>());
        DomainEvent event = new WarehouseCreatedEvent("wh-1", "Warehouse_1", Location.builder().x(1).y(1).build(), "PRODUCTION");

        warehouse.recordEvent(event);

        assertThat(warehouse.getDomainEvents()).hasSize(1).contains(event);
    }

    @Test
    void getDomainEvents_returnsUnmodifiableCopy() {
        Warehouse warehouse = buildWarehouse(new ArrayList<>());
        warehouse.recordEvent(new WarehouseCreatedEvent("wh-1", "Warehouse_1", Location.builder().x(1).y(1).build(), "PRODUCTION"));

        List<DomainEvent> events = warehouse.getDomainEvents();

        assertThat(events).hasSize(1);
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
                () -> events.add(new WarehouseCreatedEvent("wh-2", "Other", Location.builder().x(1).y(1).build(), "FACTORY")));
    }

    @Test
    void clearDomainEvents_emptiesEventList() {
        Warehouse warehouse = buildWarehouse(new ArrayList<>());
        warehouse.recordEvent(new WarehouseCreatedEvent("wh-1", "Warehouse_1", Location.builder().x(1).y(1).build(), "PRODUCTION"));

        warehouse.clearDomainEvents();

        assertThat(warehouse.getDomainEvents()).isEmpty();
    }
}