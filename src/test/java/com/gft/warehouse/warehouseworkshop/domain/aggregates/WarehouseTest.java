package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseTest {


    private static Stream<Arguments> providerGeneratedWarehouse() {
        return Stream.of(
                Arguments.of(Warehouse.builder()
                        .warehouseId( WarehouseId.builder().id(UUID.randomUUID()).build() )
                        .warehouseName("Warehouse_1")
                        .warehouseType( Type.PRODUCTION )
                        .warehouseLocation( Location.builder().x(1).y(1).build())
                        .minimumStockRules( Map.of(1,100))
                        .isStockInfinite(true)
                        .factoryId(FactoryId.builder().id(UUID.randomUUID()).build())
                        .build()),
                Arguments.of(Warehouse.builder()
                        .warehouseId( WarehouseId.builder().id(UUID.randomUUID()).build() )
                        .warehouseName("Warehouse_1")
                        .warehouseType( Type.PRODUCTION )
                        .warehouseLocation( Location.builder().x(1).y(1).build())
                        .minimumStockRules( Map.of(1,100))
                        .isStockInfinite(true)
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("providerGeneratedWarehouse")
    void generateWarehouse(Warehouse warehouse){
        assertThat( warehouse).isInstanceOf( Warehouse.class);
        assertThat( warehouse.getWarehouseId() ).isInstanceOf(WarehouseId.class).isNotNull();
        assertThat( warehouse.getWarehouseName() ).isInstanceOf(String.class).isNotNull();
        assertThat( warehouse.getWarehouseType() ).isInstanceOf(Type.class).isNotNull();
        assertThat( warehouse.getWarehouseLocation() ).isInstanceOf(Location.class).isNotNull();
        assertThat( warehouse.getMinimumStockRules() ).isInstanceOf(Map.class).isNotNull();
        assertThat( warehouse.isStockInfinite() ).isInstanceOf(Boolean.class).isNotNull();
    }

}