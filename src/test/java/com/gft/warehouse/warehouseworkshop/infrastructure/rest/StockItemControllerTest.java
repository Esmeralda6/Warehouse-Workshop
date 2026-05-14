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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockItemControllerTest {

    @InjectMocks
    private StockItemController stockItemController;

    @Mock
    private StockItemService stockItemService;

    @Test
    void getStockItems() {
        when(stockItemService.getStockItems()).thenReturn(
                List.of(StockItemDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .productId(UUID.randomUUID().toString())
                        .warehouseId(UUID.randomUUID().toString())
                        .quantity(10)
                        .minimumQuantity(2)
                        .build())
        );

        var result = stockItemController.getStockItems();

        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.getFirst())
                .isInstanceOf(StockItemDTO.class);
    }

    @Test
    void getStockItemById() {
        String id = UUID.randomUUID().toString();
        when(stockItemService.getStockItemById(id)).thenReturn(
                Optional.of(StockItemDTO.builder()
                        .id(id)
                        .productId(UUID.randomUUID().toString())
                        .warehouseId(UUID.randomUUID().toString())
                        .quantity(5)
                        .minimumQuantity(1)
                        .build())
        );

        var result = stockItemController.getStockItemById(id);

        assertThat(result)
                .isNotNull();
        assertThat(result.isPresent())
                .isTrue();
        assertThat(result.get().getId())
                .isEqualTo(id);
    }

    @Test
    void saveStockItem() {
        StockItemDTO stockItemDTO = StockItemDTO.builder()
                .id(UUID.randomUUID().toString())
                .productId(UUID.randomUUID().toString())
                .warehouseId(UUID.randomUUID().toString())
                .quantity(10)
                .minimumQuantity(2)
                .build();
        when(stockItemService.saveStockItem(stockItemDTO)).thenReturn("Stock Item saved with id: " + stockItemDTO.getId());

        var result = stockItemController.saveStockItem(stockItemDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result.substring(result.length() - stockItemDTO.getId().length()))
                .isEqualTo(stockItemDTO.getId());
    }

    @Test
    void updateStockItem() {
        String id = UUID.randomUUID().toString();
        StockItemDTO stockItemDTO = StockItemDTO.builder()
                .productId(UUID.randomUUID().toString())
                .warehouseId(UUID.randomUUID().toString())
                .quantity(20)
                .minimumQuantity(5)
                .build();
        when(stockItemService.updateStockItem(id, stockItemDTO)).thenReturn("Stock item with id " + id + " succesfully updated");

        var result = stockItemController.updateStockItem(id, stockItemDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id);
    }

    @Test
    void deleteStockItem() {
        String id = UUID.randomUUID().toString();
        when(stockItemService.deleteStockItem(id)).thenReturn("Stock item with id " + id + " succesfully deleted.");

        var result = stockItemController.deleteStockItem(id);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id);
    }
}
