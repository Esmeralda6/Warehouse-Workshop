package com.gft.warehouse.warehouseworkshop.domain.events;

public class StockChangedEvent extends DomainEvent {

    private final String productId;
    private final int    quantity;
    private final String type;

    public StockChangedEvent(String productId, int quantity, String type) {
        super();
        this.productId = productId;
        this.quantity  = quantity;
        this.type      = type;
    }

    public String getProductId() { return productId; }
    public int    getQuantity()  { return quantity; }
    public String getType()      { return type; }

    @Override
    public String getEventType() { return "warehouse.stock.changed.v1"; }
}
