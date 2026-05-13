package com.gft.warehouse.warehouseworkshop.application.service;

import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.repository.WarehouseRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Test
    void getWarehouses() {
        when( warehouseRepository.findAll() ).thenReturn(
                List.of(
                        Warehouse.builder()
                                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                                .warehouseType(Type.FACTORY)
                                .warehouseName("warehouse_1")
                                .warehouseLocation( Location.builder().x(1).y(1).build())
                                .factoryId(
                                        FactoryId.builder()
                                                .id(UUID.randomUUID()).build()
                                )
                                .build()
                )
        );

        var result = warehouseService.getWarehouses();

        assertThat( result )
                .isNotNull();
        assertThat( result.size() )
                .isEqualTo(1);
        assertThat( result.getFirst() )
                .isInstanceOf( WarehouseDTO.class );
    }

    @Test
    void getWarehouseById() {
        WarehouseId id = WarehouseId.builder().id(UUID.randomUUID()).build();
        when( warehouseRepository.findById(id)).thenReturn(
                Optional.ofNullable(Warehouse.builder()
                        .warehouseId(id)
                        .warehouseType(Type.FACTORY)
                        .warehouseLocation( Location.builder().x(1).y(1).build())
                        .factoryId(
                                FactoryId.builder()
                                        .id(UUID.randomUUID()).build()
                        )
                        .build())
        );

        var result = warehouseService.getWarehouseById( id.getId().toString() );

        assertThat( result )
                .isNotNull();
        assertThat( result.isPresent() )
                .isTrue();
        assertThat( result.get() )
                .isInstanceOf(WarehouseDTO.class);
        assertThat( result.get().getId() )
                .isEqualTo( id.getId().toString() );
    }


    private static Stream<Arguments> providerNullableId() {
        return Stream.of(
                Arguments.of(WarehouseDTO.builder()
                        .id( null )
                        .name("warehouse_1")
                        .location(LocationDTO.builder().x(1).y(2).build())
                        .type("FACTORY")
                        .build()),
                Arguments.of(WarehouseDTO.builder()
                        .id( UUID.randomUUID().toString() )
                        .name("warehouse_1")
                        .location(LocationDTO.builder().x(1).y(2).build())
                        .type("FACTORY")
                        .build())
        );
    }
    @ParameterizedTest
    @MethodSource("providerNullableId")
    void saveWarehouse(WarehouseDTO warehouseDTO) {



        when(warehouseRepository.save(Mockito.any( Warehouse.class))).thenReturn(
                WarehouseEntity.builder()
                        .id( UUID.randomUUID() )
                        .name( warehouseDTO.getName() )
                        .type(Type.valueOf(warehouseDTO.getType()))
                        .x( warehouseDTO.getLocation().getX() )
                        .y( warehouseDTO.getLocation().getY() )
                        .build()
        );

        var result = warehouseService.saveWarehouse(
                warehouseDTO
        );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat( result )
                .doesNotContain("null");
    }
}