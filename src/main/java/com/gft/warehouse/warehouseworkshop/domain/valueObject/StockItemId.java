package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class StockItemId {

    private final UUID id;


    public StockItemId(UUID id) {
        this.id = validateStockItemId(id);
    }


    private UUID validateStockItemId (UUID id){
        if (id == null){
            throw new IllegalArgumentException("The ID of the stock item can't be empty.");
        }
        return id;
    }
}
