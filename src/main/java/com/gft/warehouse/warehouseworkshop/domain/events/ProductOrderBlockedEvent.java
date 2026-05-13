package com.gft.warehouse.warehouseworkshop.domain.events;

public class ProductOrderBlockedEvent extends DomainEvent {

    public static final String DEFAULT_REASON =
            "Blocked due to insufficient materials, awaiting replenishment";

    private final String productId;
    private final String warehouseId;
    private final String reason;
    private final String   blockedSinceDay;

    public ProductOrderBlockedEvent(String productId, String warehouseId, String reason, String blockedSinceDay) {
        super();
        this.productId       = productId;
        this.warehouseId     = warehouseId;
        this.reason          = reason;
        this.blockedSinceDay = blockedSinceDay;
    }

    public String getProductId()       { return productId; }
    public String getWarehouseId()     { return warehouseId; }
    public String getReason()          { return reason; }
    public String    getBlockedSinceDay() { return blockedSinceDay; }

    @Override
    public String getEventType() { return "product.order.blocked.v1"; }
}
