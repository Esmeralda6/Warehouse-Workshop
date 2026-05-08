package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Location {
    int x;
    int y;

    public Location(int x, int y) {
        this.x = validateXandY(x);
        this.y = validateXandY(y);
    }

    private int validateXandY(int value){
        if (value < 0) {
            throw new IllegalArgumentException("Location coordinate cannot be negative");
        }
        if (value >= 100) {
            throw new IllegalArgumentException("Location coordinate cannot be higher than 99");
        }

        return value;
    }

}
