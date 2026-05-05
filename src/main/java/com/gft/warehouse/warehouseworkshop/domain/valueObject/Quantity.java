package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;

@Builder
public class Quantity {
    private final int quantity;

    public Quantity(int quantity) {
        this.quantity = quantity;
    }
}
