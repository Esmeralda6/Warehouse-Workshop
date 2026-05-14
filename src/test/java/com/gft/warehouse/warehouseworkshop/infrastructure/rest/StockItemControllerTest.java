package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.application.service.stockItem.StockItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockItemControllerTest {

    @InjectMocks
    StockItemController stockItemController;

    @Mock
    StockItemService stockItemService;

    @Test
    void getStockItems() {
        when( stockItemService.getStockItems()).thenReturn(
                List.of()
        );

        var result = stockItemController.getStockItems();

        assertThat( result )
                .isNotNull()
                .hasSizeGreaterThanOrEqualTo( 0 );
    }

    @Test
    void getStockItemById() {
        String id = "id_1";
        when( stockItemService.getStockItemById(id) ).thenReturn(
                Optional.ofNullable(
                        StockItemDTO.builder().id(id).build()
                )
        );

        var result = stockItemController.getStockItemById( id );

        assertThat( result )
                .isNotNull();
        assertThat( result.isPresent() )
                .isTrue();
        assertThat( result.get().getId() )
                .isEqualTo( id );
    }

    @Test
    void saveStockItem() {
        StockItemDTO stockItem =
                StockItemDTO.builder().id("id_1").build();

        when (stockItemService.saveStockItem( stockItem ))
                .thenReturn( "StockItem created with id: " + stockItem.getId());

        var result = stockItemController.saveStockItem( stockItem );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat(
                result.substring( result.length()- stockItem.getId().length()))
                .isEqualTo( stockItem.getId() );
    }

    @Test
    void updateStockItem() {
        StockItemDTO stockItem =
                StockItemDTO.builder().id("id_1").build();
        when (stockItemService.updateStockItem( stockItem.getId(), stockItem ))
                .thenReturn( "StockItem updated with id: " + stockItem.getId());

        var result = stockItemController.updateStockItem( stockItem.getId(), stockItem );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat(
                result.substring( result.length()- stockItem.getId().length()))
                .isEqualTo( stockItem.getId() );
    }

    @Test
    void deleteStockItem() {
        String id = "id_1";

        when (stockItemService.deleteStockItem( id ))
                .thenReturn( "StockItem deleted with id: " + id);

        var result = stockItemController.deleteStockItem(id);

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat(
                result.substring( result.length()- id.length()))
                .isEqualTo( id );
    }
}