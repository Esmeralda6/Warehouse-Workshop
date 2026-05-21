package com.gft.warehouse.warehouseworkshop.domain.events;

public class ProductChangedEvent extends DomainEvent {

    private final String productId;
    private final String productName;

    public ProductChangedEvent(String productId, String productName) {
        super();
        this.productId   = productId;
        this.productName = productName;
    }

    public String getProductId()   { return productId; }
    public String getProductName() { return productName; }

    @Override
    public String getEventType() { return "warehouse.product.changed.v1"; }
}