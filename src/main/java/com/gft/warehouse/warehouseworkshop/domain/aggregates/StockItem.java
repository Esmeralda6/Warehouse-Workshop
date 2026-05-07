package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.exceptions.InsuficientStockException;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockItem {
    private final ProductId productId;
    private Quantity quantity;

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
