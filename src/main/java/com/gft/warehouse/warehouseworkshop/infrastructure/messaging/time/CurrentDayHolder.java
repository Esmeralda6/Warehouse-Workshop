package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.time;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class CurrentDayHolder {

    @Getter
    private static volatile int currentDay = 0;

    public void update(int day) {
        currentDay = day;
    }

}
