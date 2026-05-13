package com.gft.warehouse.warehouseworkshop.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;

public class WarehouseCreatedEvent extends DomainEvent {

    private final String   warehouseId;
    private final String   warehouseName;
    private final Location location;
    private final String   warehouseType;

    public WarehouseCreatedEvent(String warehouseId, String warehouseName, Location location, String warehouseType) {
        super();
        this.warehouseId   = warehouseId;
        this.warehouseName = warehouseName;
        this.location      = location;
        this.warehouseType = warehouseType;
    }

    public String   getWarehouseId()   { return warehouseId; }
    @JsonProperty("name")
    public String   getWarehouseName() { return warehouseName; }
    public Location getLocation()      { return location; }
    public String   getWarehouseType() { return warehouseType; }

    @Override
    public String getEventType() {
        return "warehouse.registered.v1";
    }
}
