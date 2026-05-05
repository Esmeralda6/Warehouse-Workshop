package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import lombok.Builder;
import lombok.Getter;

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

    public Warehouse(WarehouseId warehouseId, String warehouseName, Type warehouseType, Location warehouseLocation, Map minimumStockRules, boolean isStockInfinite) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.warehouseType = warehouseType;
        this.warehouseLocation = warehouseLocation;
        this.minimumStockRules = minimumStockRules;
        this.isStockInfinite = isStockInfinite;
    }
}
