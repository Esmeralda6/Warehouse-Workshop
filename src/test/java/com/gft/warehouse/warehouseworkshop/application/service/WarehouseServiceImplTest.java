package com.gft.warehouse.warehouseworkshop.application.service;

import com.gft.warehouse.warehouseworkshop.application.dto.FactoryIdDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.application.service.warehouse.WarehouseServiceImpl;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;
import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.ports.EventPublisher;
import com.gft.warehouse.warehouseworkshop.domain.repository.WarehouseRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Test
    void getWarehouses() {
        when( warehouseRepository.findAll() ).thenReturn(
                List.of(
                        Warehouse.builder()
                                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                                .warehouseType(WarehouseType.FACTORY)
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
                        .warehouseType(WarehouseType.FACTORY)
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
                        .warehouseType(WarehouseType.valueOf(warehouseDTO.getType()))
                        .x( warehouseDTO.getLocation().getX() )
                        .y( warehouseDTO.getLocation().getY() )
                        .build()
        );

        Assertions.assertDoesNotThrow(() -> warehouseService.saveWarehouse( warehouseDTO ));

        var result = warehouseService.saveWarehouse(
                warehouseDTO
        );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat( result )
                .doesNotContain("null");
    }

    private static Stream<Arguments> providerNullableFactoryId() {
        return Stream.of(
                Arguments.of(
                        UUID.randomUUID().toString(),
                        WarehouseDTO.builder()
                            .name("warehouse_1")
                            .location(LocationDTO.builder().x(1).y(2).build())
                            .type("FACTORY")
                            .factoryId(null)
                            .build()),
                Arguments.of(
                        UUID.randomUUID().toString(),
                        WarehouseDTO.builder()
                            .name("warehouse_1")
                            .location(LocationDTO.builder().x(1).y(2).build())
                            .type("FACTORY")
                            .factoryId( UUID.randomUUID().toString() )
                            .build()),
                Arguments.of(
                        UUID.randomUUID().toString(),
                        WarehouseDTO.builder()
                                .name("warehouse_1")
                                .location(LocationDTO.builder().x(1).y(2).build())
                                .type("FACTORY")
                                .factoryId( "" )
                                .build())
        );
    }
    @ParameterizedTest
    @MethodSource("providerNullableFactoryId")
    void updateWarehouseWhenFound(String id, WarehouseDTO warehouseDTO) {
        WarehouseId warehouseId = WarehouseId.builder().id(UUID.fromString(id)).build();
        when( warehouseRepository.findById(warehouseId)).thenReturn(
                Optional.ofNullable(Warehouse.builder()
                        .warehouseId( warehouseId )
                        .build())
        );

        when(warehouseRepository.save(Mockito.any( Warehouse.class))).thenReturn(
                WarehouseEntity.builder()
                        .id( warehouseId.getId() )
                        .name( warehouseDTO.getName() )
                        .warehouseType(WarehouseType.valueOf(warehouseDTO.getType()))
                        .x( warehouseDTO.getLocation().getX() )
                        .y( warehouseDTO.getLocation().getY() )
                        .build()
        );

        Assertions.assertDoesNotThrow(() -> warehouseService.updateWarehouse(id, warehouseDTO ));

        var result = warehouseService.updateWarehouse(
                id, warehouseDTO
        );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat( result )
                .doesNotContain("null");

    }

    @Test
    void updateWarehouseWhenNotFound() {
        WarehouseId warehouseId = WarehouseId.builder().id(UUID.randomUUID()).build();
        WarehouseDTO warehouseDTO = WarehouseDTO.builder().build();
        when( warehouseRepository.findById( warehouseId ) ).thenReturn(
                Optional.empty()
        );

        var result = warehouseService.updateWarehouse( warehouseId.getId().toString(), warehouseDTO);

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat( result )
                .contains( warehouseId.getId() + " not found.");
    }

    @Test
    void deleteWarehouseWhenFound() {
        WarehouseId warehouseId = WarehouseId.builder().id(UUID.randomUUID()).build();
        when( warehouseRepository.findById( warehouseId ) ).thenReturn(
                Optional.of( Warehouse.builder().warehouseId(warehouseId).build())
        );

        var result = warehouseService.deleteWarehouse( warehouseId.getId().toString());

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat( result )
                .contains( warehouseId.getId() + " succesfully deleted.");
    }

    @Test
    void deleteWarehouseWhenNotFound() {
        WarehouseId warehouseId = WarehouseId.builder().id(UUID.randomUUID()).build();
        when( warehouseRepository.findById( warehouseId ) ).thenReturn(
                Optional.empty()
        );

        var result = warehouseService.deleteWarehouse( warehouseId.getId().toString());

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat( result )
                .contains( warehouseId.getId() + " was not found.");
    }

    @Test
    void findAvailableWarehouse() {
        WarehouseId warehouseId = WarehouseId.builder().id(UUID.randomUUID()).build();

        when( warehouseRepository.findAvailable()).thenReturn(
                Optional.of( Warehouse.builder()
                        .warehouseId( warehouseId )
                        .warehouseType( WarehouseType.FACTORY )
                        .warehouseLocation( Location.builder().x(1).y(1).build())
                        .factoryId(
                                FactoryId.builder().id(null).build()
                        )
                        .build())
        );

        var result = warehouseService.findAvailableWarehouse();

        assertThat( result )
                .isNotNull();
        assertThat( result.isPresent() )
                .isTrue();
        assertThat( result.get() )
                .isInstanceOf(WarehouseDTO.class);
        assertThat( result.get().getId() )
                .isEqualTo( warehouseId.getId().toString() );
        assertThat( result.get().getFactoryId() )
                .isNull();
    }

    @Test
    void assignFactoryIdWhenFound() {
        WarehouseId warehouseId = WarehouseId.builder().id(UUID.randomUUID()).build();
        FactoryIdDTO factoryId = FactoryIdDTO.builder().factoryId( UUID.randomUUID().toString()  ).build();
        when( warehouseRepository.findById( warehouseId ) ).thenReturn(
                Optional.of( Warehouse.builder().warehouseId(warehouseId).build())
        );

        when(warehouseRepository.save(Mockito.any( Warehouse.class))).thenReturn(
                WarehouseEntity.builder()
                        .id( warehouseId.getId() )
                        .warehouseType( WarehouseType.FACTORY)
                        .factoryId( UUID.fromString(factoryId.getFactoryId()) )
                        .build()
        );

        var result = warehouseService.assignFactoryId( warehouseId.getId().toString(), factoryId);

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat( result )
                .contains( warehouseId.getId() + " succesfully assigned to factory with id " + factoryId);
    }

    @Test
    void assignFactoryIdWhenNotFound() {
        WarehouseId warehouseId = WarehouseId.builder().id(UUID.randomUUID()).build();
        FactoryIdDTO factoryId = FactoryIdDTO.builder().factoryId(UUID.randomUUID().toString()).build();
        when(warehouseRepository.findById(warehouseId)).thenReturn(
                Optional.empty()
        );

        var result = warehouseService.assignFactoryId(warehouseId.getId().toString(), factoryId);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(warehouseId.getId() + " not found.");
    }
    @Test
    void saveWarehouse_publishesWarehouseCreatedEvent() {
        WarehouseDTO warehouseDTO = WarehouseDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("warehouse_1")
                .location(LocationDTO.builder().x(1).y(2).build())
                .type("FACTORY")
                .build();

        when(warehouseRepository.save( Mockito.any( Warehouse.class))).thenReturn(
                WarehouseEntity.builder()
                        .id( UUID.randomUUID() )
                        .name( warehouseDTO.getName() )
                        .warehouseType(WarehouseType.valueOf(warehouseDTO.getType()))
                        .x( warehouseDTO.getLocation().getX() )
                        .y( warehouseDTO.getLocation().getY() )
                        .build()
        );

        warehouseService.saveWarehouse(warehouseDTO);

        ArgumentCaptor<DomainEvent> captor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(eventPublisher).publish(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(WarehouseCreatedEvent.class);
        WarehouseCreatedEvent event = (WarehouseCreatedEvent) captor.getValue();
        assertThat(event.getWarehouseName()).isEqualTo("warehouse_1");
        assertThat(event.getWarehouseType()).isEqualTo("FACTORY");
    }
}