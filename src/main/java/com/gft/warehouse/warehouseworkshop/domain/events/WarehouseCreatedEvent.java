package com.gft.warehouse.warehouseworkshop.domain.events;

public class WarehouseCreatedEvent extends DomainEvent {

    private final String warehouseId;
    private final String warehouseName;
    private final String warehouseType;

    public WarehouseCreatedEvent(String warehouseId, String warehouseName, String warehouseType) {
        super();
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.warehouseType = warehouseType;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public String getWarehouseType() {
        return warehouseType;
    }

    @Override
    public String getEventType() {
        return "warehouse.registered.v1";
    }
}
