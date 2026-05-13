package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.time;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentDayHolderTest {

    private final CurrentDayHolder holder = new CurrentDayHolder();

    @Test
    void initialDay_isZero() {
        assertThat(holder.getCurrentDay()).isZero();
    }

    @Test
    void update_changesCurrentDay() {
        holder.update(10);

        assertThat(holder.getCurrentDay()).isEqualTo(10);
    }

    @Test
    void update_multipleTimesKeepsLatest() {
        holder.update(3);
        holder.update(7);

        assertThat(holder.getCurrentDay()).isEqualTo(7);
    }
}
