package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.time;

import org.springframework.stereotype.Component;

@Component
public class CurrentDayHolder {

    private volatile int currentDay = 0;

    public void update(int day) {
        this.currentDay = day;
    }

    public int getCurrentDay() {
        return currentDay;
    }
}
