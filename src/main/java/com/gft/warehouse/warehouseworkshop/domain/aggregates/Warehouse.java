package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;
import com.gft.warehouse.warehouseworkshop.domain.services.StockChecker;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Warehouse {
    private final WarehouseId warehouseId;
    private final String warehouseName;
    private final Type warehouseType;
    private final Location warehouseLocation;
    private final FactoryId factoryId;

    @Builder.Default
    private List<StockItem> stock = new ArrayList<>();

    @Builder.Default
    private List<DomainEvent> domainEvents = new ArrayList<>();

    public void recordEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public boolean checkOwnStock(List<StockItem> items, StockChecker checker) {
        return checker.checkOwnStock(this.stock, items);
    }

    public List<StockItem> consumeStock(List<StockItem> items) {
        items.forEach(requested ->
                stock.stream()
                        .filter(s -> s.hasProduct(requested.getProductId()))
                        .findFirst()
                        .ifPresent(s -> s.subtract(requested.getQuantity()))
        );
        return items;
    }

    public void receiveDelivery(List<StockItem> items) {
        items.forEach(delivered ->
                stock.stream()
                        .filter(s -> s.hasProduct(delivered.getProductId()))
                        .findFirst()
                        .ifPresentOrElse(
                                s -> s.add(delivered.getQuantity()),
                                () -> stock.add(delivered)
                        )
        );
    }

    public List<StockItem> dispatchItems(List<StockItem> items) {
        return consumeStock(items);
    }
}
