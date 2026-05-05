package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WarehouseId {
    private final String id;

    public WarehouseId(String id) {
        this.id = validate(id);
    }

    private String validate(String id){
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("WarehouseId cannot be null or blank");
        }
        return id;
    }
}
