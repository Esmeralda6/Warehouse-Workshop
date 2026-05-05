package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;

@Builder
public class Location {
    private final int x;
    private final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
