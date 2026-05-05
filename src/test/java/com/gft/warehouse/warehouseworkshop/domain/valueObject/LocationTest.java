package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocationTest {

    @Test
    void shouldCreateLocationWithValidCoordinates() {
        Location location = Location.builder().x(5).y(10).build();

        assertThat(location).isNotNull().isInstanceOf(Location.class);
        assertThat(location.getX()).isEqualTo(5);
        assertThat(location.getY()).isEqualTo(10);
    }

    @Test
    void shouldCreateLocationWithZeroCoordinates() {
        Location location = Location.builder().x(0).y(0).build();

        assertThat(location).isNotNull();
        assertThat(location.getX()).isZero();
        assertThat(location.getY()).isZero();
    }

    @Test
    void shouldThrowExceptionWhenXIsNegative() {
        assertThatThrownBy(() -> Location.builder().x(-1).y(5).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location x coordinate cannot be negative");
    }

    @Test
    void shouldThrowExceptionWhenYIsNegative() {
        assertThatThrownBy(() -> Location.builder().x(5).y(-1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location y coordinate cannot be negative");
    }

    @Test
    void shouldThrowExceptionWhenXIsHigherThanOnehundred(){
        assertThatThrownBy(() -> Location.builder().x(100).y(5).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location x coordinate cannot be higher than 100");
    }

    @Test
    void shouldThrowExceptionWhenYIsHigherThanOnehundred(){
        assertThatThrownBy(() -> Location.builder().x(5).y(100).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location y coordinate cannot be higher than 100");
    }
}