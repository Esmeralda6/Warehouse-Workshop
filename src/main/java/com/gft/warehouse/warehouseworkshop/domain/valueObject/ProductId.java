package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductId {
    private final String id;

    public ProductId(String id) {

        this.id = validateProductId(id);
    }

    private String validateProductId (String id){
        if (id == null){
            throw new IllegalArgumentException("The ID of the product can't be empty.");
        }

        if ( id.isBlank() ){
            throw new IllegalArgumentException("The ID of the product can't be blank.");
        }
        return id;
    }
}
