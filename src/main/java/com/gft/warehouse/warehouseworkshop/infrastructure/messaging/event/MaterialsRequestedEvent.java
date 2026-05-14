package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MaterialsRequestedEvent {

    private List<Item> items;

    @Data
    @NoArgsConstructor
    public static class Item {
        private String productId;
        private int    quantity;
    }
}
