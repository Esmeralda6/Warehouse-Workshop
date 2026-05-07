package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.StockItemId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockItem {
    private final StockItemId stockItemId;
    private final ProductId productId;
    private final Quantity quantity;
    private final WarehouseId warehouseId;
    private final Quantity minimumQuantityRule;

}
