package com.gft.warehouse.warehouseworkshop.application.service.warehouse;

import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseMapperUtilsTest {

    @Test
    void toDTO_fromDomain_withFactoryId() {
        UUID warehouseUUID = UUID.randomUUID();
        UUID factoryUUID = UUID.randomUUID();
        Warehouse warehouse = Warehouse.builder()
                .warehouseId(WarehouseId.builder().id(warehouseUUID).build())
                .warehouseName("warehouse_1")
                .warehouseType(WarehouseType.FACTORY)
                .warehouseLocation(Location.builder().x(1).y(2).build())
                .factoryId(FactoryId.builder().id(factoryUUID).build())
                .build();

        WarehouseDTO result = WarehouseMapperUtils.toDTO(warehouse);

        assertThat(result.getId()).isEqualTo(warehouseUUID.toString());
        assertThat(result.getName()).isEqualTo("warehouse_1");
        assertThat(result.getType()).isEqualTo("FACTORY");
        assertThat(result.getLocation().getX()).isEqualTo(1);
        assertThat(result.getLocation().getY()).isEqualTo(2);
        assertThat(result.getFactoryId()).isEqualTo(factoryUUID.toString());
    }

    @Test
    void toDTO_fromDomain_withNullFactoryId() {
        Warehouse warehouse = Warehouse.builder()
                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                .warehouseName("warehouse_1")
                .warehouseType(WarehouseType.FACTORY)
                .warehouseLocation(Location.builder().x(1).y(2).build())
                .factoryId(FactoryId.builder().id(null).build())
                .build();

        WarehouseDTO result = WarehouseMapperUtils.toDTO(warehouse);

        assertThat(result.getFactoryId()).isNull();
    }

    @Test
    void toDto_fromEntity_withFactoryId() {
        UUID warehouseUUID = UUID.randomUUID();
        UUID factoryUUID = UUID.randomUUID();
        WarehouseEntity entity = WarehouseEntity.builder()
                .id(warehouseUUID)
                .name("warehouse_1")
                .warehouseType(WarehouseType.FACTORY)
                .x(3)
                .y(4)
                .factoryId(factoryUUID)
                .build();

        WarehouseDTO result = WarehouseMapperUtils.toDto(entity);

        assertThat(result.getId()).isEqualTo(warehouseUUID.toString());
        assertThat(result.getName()).isEqualTo("warehouse_1");
        assertThat(result.getType()).isEqualTo("FACTORY");
        assertThat(result.getLocation().getX()).isEqualTo(3);
        assertThat(result.getLocation().getY()).isEqualTo(4);
        assertThat(result.getFactoryId()).isEqualTo(factoryUUID.toString());
    }

    @Test
    void toDto_fromEntity_withNullFactoryId() {
        WarehouseEntity entity = WarehouseEntity.builder()
                .id(UUID.randomUUID())
                .name("warehouse_1")
                .warehouseType(WarehouseType.FACTORY)
                .x(1)
                .y(1)
                .factoryId(null)
                .build();

        WarehouseDTO result = WarehouseMapperUtils.toDto(entity);

        assertThat(result.getFactoryId()).isNull();
    }

    @Test
    void toDomain_fromEntity() {
        UUID warehouseUUID = UUID.randomUUID();
        UUID factoryUUID = UUID.randomUUID();
        WarehouseEntity entity = WarehouseEntity.builder()
                .id(warehouseUUID)
                .name("warehouse_1")
                .warehouseType(WarehouseType.FACTORY)
                .x(1)
                .y(2)
                .factoryId(factoryUUID)
                .build();

        Warehouse result = WarehouseMapperUtils.toDomain(entity);

        assertThat(result.getWarehouseId().getId()).isEqualTo(warehouseUUID);
        assertThat(result.getWarehouseName()).isEqualTo("warehouse_1");
        assertThat(result.getWarehouseType()).isEqualTo(WarehouseType.FACTORY);
        assertThat(result.getWarehouseLocation().getX()).isEqualTo(1);
        assertThat(result.getWarehouseLocation().getY()).isEqualTo(2);
        assertThat(result.getFactoryId().getId()).isEqualTo(factoryUUID);
    }

    private static Stream<Arguments> providerNullableId() {
        return Stream.of(
                Arguments.of(WarehouseDTO.builder()
                        .id( UUID.randomUUID().toString() )
                        .name("warehouse_1")
                        .location(LocationDTO.builder().x(1).y(2).build())
                        .type("FACTORY")
                        .factoryId( UUID.randomUUID().toString())
                        .build()),
                Arguments.of(WarehouseDTO.builder()
                        .id( UUID.randomUUID().toString() )
                        .name("warehouse_3")
                        .location(LocationDTO.builder().x(1).y(2).build())
                        .type("FACTORY")
                        .factoryId(null)
                        .build())
        );
    }
    @ParameterizedTest
    @MethodSource("providerNullableId")
    void toDomain_fromDTO( WarehouseDTO warehouseDTO) {

        Warehouse result = WarehouseMapperUtils.toDomain(warehouseDTO);

        assertThat(result.getWarehouseId().getId()).isEqualTo(UUID.fromString(warehouseDTO.getId()));
        assertThat(result.getWarehouseName()).isEqualTo( warehouseDTO.getName() );
        assertThat(result.getWarehouseType()).isEqualTo(WarehouseType.FACTORY);
        assertThat(result.getWarehouseLocation().getX()).isEqualTo(1);
        assertThat(result.getWarehouseLocation().getY()).isEqualTo(2);
        assertThat(result.getFactoryId()).isNotNull();
    }

    @Test
    void toEntity() {
        UUID warehouseUUID = UUID.randomUUID();
        UUID factoryUUID = UUID.randomUUID();
        Warehouse warehouse = Warehouse.builder()
                .warehouseId(WarehouseId.builder().id(warehouseUUID).build())
                .warehouseName("warehouse_1")
                .warehouseType(WarehouseType.FACTORY)
                .warehouseLocation(Location.builder().x(5).y(6).build())
                .factoryId(FactoryId.builder().id(factoryUUID).build())
                .build();

        WarehouseEntity result = WarehouseMapperUtils.toEntity(warehouse);

        assertThat(result.getId()).isEqualTo(warehouseUUID);
        assertThat(result.getName()).isEqualTo("warehouse_1");
        assertThat(result.getWarehouseType()).isEqualTo(WarehouseType.FACTORY);
        assertThat(result.getX()).isEqualTo(5);
        assertThat(result.getY()).isEqualTo(6);
        assertThat(result.getFactoryId()).isEqualTo(factoryUUID);
    }
}