package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FactoryRegisteredEvent {

    private String factoryId;
    private String warehouseId;
}
