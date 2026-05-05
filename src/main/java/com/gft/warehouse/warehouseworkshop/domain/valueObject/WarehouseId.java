package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;

@Builder
public class WarehouseId {
    private final String id;

    public WarehouseId(String id) {
        this.id = id;
    }
}
