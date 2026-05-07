package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.application.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WarehouseControllerTest {

    @InjectMocks
    private WarehouseController warehouseController;

    @Mock
    private WarehouseService warehouseService;

    @Test
    void getWarehouses(){
        when( warehouseService.getWarehouses()).thenReturn(
                List.of()
        );

        var result = warehouseController.getWarehouses();

        assertThat( result )
                .isNotNull();
        assertThat( result.size() )
                .isGreaterThanOrEqualTo( 0 );
    }

    @Test
    void getWarehouseById(){
        String id = "id_1";
        when( warehouseService.getWarehouseById(id) ).thenReturn(
                Optional.ofNullable(
                        WarehouseDTO.builder().id(id).build()
                )
        );

        var result = warehouseController.getWarehouseById( id );

        assertThat( result )
                .isNotNull();
        assertThat( result.isPresent() )
                .isTrue();
        assertThat( result.get().getId() )
                .isEqualTo( id );
    }

    @Test
    void saveWarehouse(){
        WarehouseDTO warehouse =
                WarehouseDTO.builder().id("id_1").build();

        when (warehouseService.saveWarehouse( warehouse ))
                .thenReturn( "Warehouse created with id: " + warehouse.getId());

        var result = warehouseController.saveWarehouse( warehouse );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat(
                result.substring( result.length()- warehouse.getId().length()))
                .isEqualTo( warehouse.getId() );
    }
}