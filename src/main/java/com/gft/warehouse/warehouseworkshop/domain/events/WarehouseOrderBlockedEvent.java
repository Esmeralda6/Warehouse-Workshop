package com.gft.warehouse.warehouseworkshop.domain.events;

public class WarehouseOrderBlockedEvent extends DomainEvent {

    private final String orderId;

    public WarehouseOrderBlockedEvent(String orderId) {
        super();
        this.orderId = orderId;
    }

    public String getOrderId() { return orderId; }

    @Override
    public String getEventType() { return "warehouse.order.blocked.v1"; }
}
