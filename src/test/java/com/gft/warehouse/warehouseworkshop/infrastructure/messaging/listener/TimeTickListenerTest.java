package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.TimeTickEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.time.CurrentDayHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TimeTickListenerTest {

    private final CurrentDayHolder currentDayHolder = new CurrentDayHolder();
    private final TimeTickListener listener         = new TimeTickListener(currentDayHolder);

    @Test
    void onTimeTick_doesNotThrow() {
        TimeTickEvent event = new TimeTickEvent();
        event.setTick(5);

        assertThatCode(() -> listener.onTimeTick(event)).doesNotThrowAnyException();
    }

    @Test
    void onTimeTick_updatesCurrentDayHolder() {
        TimeTickEvent event = new TimeTickEvent();
        event.setTick(12);

        listener.onTimeTick(event);

        assertThat(currentDayHolder.getCurrentDay()).isEqualTo(12);
    }
}
