package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Quantity {
    private final int quantity;

    public Quantity(int quantity) {
        this.quantity = validateQuantity(quantity);
    }

    private int validateQuantity(int quantity){
        if (quantity < 0){
            throw new IllegalArgumentException("The quantity can't be negative.");
        }
        return quantity;
    }
}
