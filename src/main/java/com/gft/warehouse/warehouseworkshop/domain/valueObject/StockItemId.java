package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class StockItemId {
    UUID id;

    public StockItemId(UUID id) {

        this.id = validateProductId(id);
    }

    private UUID validateProductId (UUID id){
        if (id == null){
            throw new IllegalArgumentException("The ID of the stock item can't be empty.");
        }
        return id;
    }
}
