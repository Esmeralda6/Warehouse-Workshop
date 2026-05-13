package com.gft.warehouse.warehouseworkshop.domain.events;

import java.util.List;

public class MaterialsGivenEvent extends DomainEvent {

    private final List<Item> items;

    public MaterialsGivenEvent(List<Item> items) {
        super();
        this.items = items;
    }

    public List<Item> getItems() { return items; }

    @Override
    public String getEventType() { return "materials.given.v1"; }

    public static class Item {
        private final String productId;
        private final int    quantity;

        public Item(String productId, int quantity) {
            this.productId = productId;
            this.quantity  = quantity;
        }

        public String getProductId() { return productId; }
        public int    getQuantity()  { return quantity; }
    }
}
