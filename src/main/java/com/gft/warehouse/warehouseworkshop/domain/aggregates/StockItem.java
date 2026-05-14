package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.exceptions.InsuficientStockException;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.StockItemId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StockItem {
    private final StockItemId stockItemId;
    private final ProductId productId;
    private Quantity quantity;
    private final WarehouseId warehouseId;
    private Quantity minimumQuantityRule;

    public boolean isEnough(Quantity needed) {
        return this.quantity.getValue() >= needed.getValue();
    }

    public void add(Quantity qty) {
        this.quantity = Quantity.builder().value(this.quantity.getValue() + qty.getValue()).build();
    }

    public void subtract(Quantity qty) {
        if (qty.getValue() > this.quantity.getValue()) {
            throw new InsuficientStockException("Not enough stock to subtract");
        }
        this.quantity = Quantity.builder().value(this.quantity.getValue() - qty.getValue()).build();
    }

    public boolean hasProduct(ProductId productId) {
        return this.productId.getId().equals(productId.getId());
    }

    
}
