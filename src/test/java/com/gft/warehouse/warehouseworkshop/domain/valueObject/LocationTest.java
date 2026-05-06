package com.gft.warehouse.warehouseworkshop.domain.valueObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    private static Stream<Arguments> providerWhenIsNegative() {
        return Stream.of(
                Arguments.of(-1, 2),
                Arguments.of(2, -1),
                Arguments.of(-1, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("providerWhenIsNegative")
    void shouldThrowExceptionWhenXOrYIsNegative(int x, int y) {
        assertThatThrownBy(() -> Location.builder().x(x).y(y).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location coordinate cannot be negative");
    }

    private static Stream<Arguments> providerHigherThanNinetyNine(){
        return Stream.of(
                Arguments.of(100, 1),
                Arguments.of(1, 100),
                Arguments.of(100, 100)
        );
    }

    @ParameterizedTest
    @MethodSource("providerHigherThanNinetyNine")
    void shouldThrowExceptionWhenXIsHigherThanNinetyNine(int x, int y){
        assertThatThrownBy(() -> Location.builder().x(x).y(y).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location coordinate cannot be higher than 99");
    }
}