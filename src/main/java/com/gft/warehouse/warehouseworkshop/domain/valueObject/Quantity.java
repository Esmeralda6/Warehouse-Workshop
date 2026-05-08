package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Quantity {
    int value;

    public Quantity(int value) {
        this.value = validateQuantity(value);
    }

    private int validateQuantity(int value){
        if (value < 0){
            throw new IllegalArgumentException("The quantity can't be negative.");
        }
        return value;
    }
}
