package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
public class Warehouse {
    private final WarehouseId warehouseId;
    private final String warehouseName;
    private final Type warehouseType;
    private final Location warehouseLocation;
    private final boolean isStockInfinite;
    private final FactoryId factoryId;

    //private List<StockItemId>

}
