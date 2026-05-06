package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ProductId {
    private final UUID id;

    public ProductId(UUID id) {

        this.id = validateProductId(id);
    }

    private UUID validateProductId (UUID id){
        if (id == null){
            throw new IllegalArgumentException("The ID of the product can't be empty.");
        }
        return id;
    }
}
