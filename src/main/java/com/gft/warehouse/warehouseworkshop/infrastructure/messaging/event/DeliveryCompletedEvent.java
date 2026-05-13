package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DeliveryCompletedEvent {

    private String     shipmentId;
    private String     truckId;
    private List<Item> items;
    private Location   location;
    private int        completedAt;

    @Data
    @NoArgsConstructor
    public static class Item {
        private String materialType;
        private int    quantity;
    }

    @Data
    @NoArgsConstructor
    public static class Location {
        private double x;
        private double y;
    }
}
