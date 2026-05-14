package com.gft.warehouse.warehouseworkshop.domain.events;

import java.util.List;

public class ShipmentRequestedEvent extends DomainEvent {

    private final String     shipmentId;
    private final String     originId;
    private final String     destinationId;
    private final List<Item> items;
    private final int        requestedAt;

    public ShipmentRequestedEvent(String shipmentId, String originId, String destinationId,
                                  List<Item> items, int requestedAt) {
        super();
        this.shipmentId     = shipmentId;
        this.originId       = originId;
        this.destinationId  = destinationId;
        this.items          = items;
        this.requestedAt    = requestedAt;
    }

    public String     getShipmentId()    { return shipmentId; }
    public String     getOriginId()      { return originId; }
    public String     getDestinationId() { return destinationId; }
    public List<Item> getItems()         { return items; }
    public int        getRequestedAt()   { return requestedAt; }

    @Override
    public String getEventType() { return "shipment.requested.v1"; }

    public static class Item {
        private final String materialType;
        private final int    quantity;

        public Item(String materialType, int quantity) {
            this.materialType = materialType;
            this.quantity     = quantity;
        }

        public String getMaterialType() { return materialType; }
        public int    getQuantity()     { return quantity; }
    }
}
