package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;

@Builder
public class ProductId {
    private final String id;

    public ProductId(String id) {
        this.id = id;
    }
}
