package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class FactoryId {
    UUID id;

    //No validations:
    //FactoryId.id can be null, so that the warehouse is created first and afterwards a Factory latches onto this.
}
