package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockItem {
    private final ProductId productId;
    private final Quantity quantity;

    public StockItem(ProductId productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
