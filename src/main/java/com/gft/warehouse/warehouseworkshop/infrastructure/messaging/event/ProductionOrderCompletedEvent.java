package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductionOrderCompletedEvent {
    private String warehouseOrderId;
    private String productionOrderId;
    private String productId;
    private String factoryAsign;
    private int    quantity;
    private String status;
}
