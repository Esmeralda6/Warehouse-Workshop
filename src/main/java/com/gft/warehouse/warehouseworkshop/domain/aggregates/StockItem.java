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
        return this.quantity.getQuantity() >= needed.getQuantity();
    }

    public void add(Quantity qty) {
        this.quantity = Quantity.builder().quantity(this.quantity.getQuantity() + qty.getQuantity()).build();
    }

    public void subtract(Quantity qty) {
        if (qty.getQuantity() > this.quantity.getQuantity()) {
            throw new InsuficientStockException("Not enough stock to subtract");
        }
        this.quantity = Quantity.builder().quantity(this.quantity.getQuantity() - qty.getQuantity()).build();
    }

    public boolean hasProduct(ProductId productId) {
        return this.productId.getId().equals(productId.getId());
    }

    
}
