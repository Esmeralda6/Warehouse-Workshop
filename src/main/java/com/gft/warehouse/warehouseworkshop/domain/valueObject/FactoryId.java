package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Builder
@Getter
public class FactoryId {

    private final UUID id;

    public FactoryId(UUID id) {
        this.id = validate(id);
    }

    private UUID validate(UUID id){
        if (id == null) {
            throw new IllegalArgumentException("FactoryId cannot be null");
        }
        return id;
    }

}
