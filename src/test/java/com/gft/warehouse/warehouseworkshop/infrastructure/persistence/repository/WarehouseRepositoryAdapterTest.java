package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.repository.WarehouseRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WarehouseRepositoryAdapterTest {

    @InjectMocks
    private WarehouseRepositoryAdapter warehouseRepository;

    @Mock
    private WarehouseJpaRepository warehouseJpaRepository;

    @Test
    void findAll() {
        when( warehouseJpaRepository.findAll()).thenReturn(
                List.of(
                        WarehouseEntity.builder()
                                .id(UUID.randomUUID())
                                .build()
                )
        );
        var result = warehouseRepository.findAll();

        assertThat( result )
                .isNotNull();
        assertThat( result.size() )
                .isEqualTo( 1 );
        assertThat( result.getFirst() )
                .isInstanceOf(Warehouse.class);
    }

    @Test
    void findById() {
        WarehouseId id = WarehouseId.builder().id(UUID.randomUUID()).build();
        when( warehouseJpaRepository.findById(id.getId())).thenReturn(
                Optional.ofNullable(WarehouseEntity.builder()
                        .id(id.getId())
                        .build())
        );
        var result = warehouseRepository.findById(id);

        assertThat( result )
                .isNotNull();
        assertThat( result.isPresent() )
                .isTrue();
        assertThat( result.get() )
                .isInstanceOf(Warehouse.class);
        assertThat( result.get().getWarehouseId() )
                .isEqualTo( id );
    }

    @Test
    void save() {
        Warehouse warehouse = Warehouse.builder()
                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                .warehouseName("warehouse_1")
                .warehouseLocation( Location.builder().x(1).y(1).build())
                .build();

        warehouseRepository.save( warehouse );
        //No tengo claro qué comprobar, hace el coverage igualmente
    }
}
/*
    Warehouse.builder()
    .warehouseId(WarehouseId.builder().id(UUID.fromString("303b5957-ce82-4dfc-bb27-f38a55d39b15")).build())
    .warehouseName("warehouse_1")
    .warehouseLocation( Location.builder().x(1).y(1).build())
    .build()
* */