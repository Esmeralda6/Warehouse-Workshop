package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.UUID;

@Builder
@Getter
@Value
public class WarehouseId {
    private final UUID id;

    public WarehouseId(UUID id) {
        this.id = validate(id);
    }

    private UUID validate(UUID id){
        if (id == null) {
            throw new IllegalArgumentException("WarehouseId cannot be null");
        }
        return id;
    }
}
