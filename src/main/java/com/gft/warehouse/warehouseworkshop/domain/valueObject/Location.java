package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Location {
    private final int x;
    private final int y;

    public Location(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("Location x coordinate cannot be negative");
        }
        if (y < 0) {
            throw new IllegalArgumentException("Location y coordinate cannot be negative");
        }
        if (x > 100) {
            throw new IllegalArgumentException("Location x cannot be higher than 100");
        }
        if (y > 100) {
            throw new IllegalArgumentException("Location y cannot be higher than 100");
        }
        this.x = x;
        this.y = y;
    }
}
