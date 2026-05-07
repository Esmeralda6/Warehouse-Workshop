package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.services.ReplenishmentPolicy;
import com.gft.warehouse.warehouseworkshop.domain.services.StockChecker;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class Warehouse {
    private final WarehouseId warehouseId;
    private final String warehouseName;
    private final Type warehouseType;
    private final Location warehouseLocation;
    private final Map minimumStockRules;
    private final boolean isStockInfinite;
    private final FactoryId factoryId;

    @Builder.Default
    private List<StockItem> stock = new ArrayList<>();

    public boolean checkOwnStock(List<StockItem> items, StockChecker checker) {
        return checker.checkOwnStock(this.stock, items);
    }

    public void consumeStock(List<StockItem> items) {
        items.forEach(requested ->
                stock.stream()
                        .filter(s -> s.hasProduct(requested.getProductId()))
                        .findFirst()
                        .ifPresent(s -> s.subtract(requested.getQuantity()))
        );
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

    public boolean needsReplenishment(ReplenishmentPolicy policy) {
        if (isStockInfinite) return false;
        return policy.shouldReplenish(this.stock, this.minimumStockRules);
    }

    public void dispatchItems(List<StockItem> items) {
        consumeStock(items);
    }
}