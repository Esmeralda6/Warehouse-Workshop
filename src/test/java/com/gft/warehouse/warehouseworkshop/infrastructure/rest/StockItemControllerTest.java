package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.service.stockItem.StockItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StockItemControllerTest {

    @InjectMocks
    StockItemController stockItemController;

    @Mock
    StockItemService stockItemService;

    @Test
    void getStockItems() {
    }

    @Test
    void getStockItemById() {
    }

    @Test
    void saveStockItem() {
    }

    @Test
    void updateStockItem() {
    }

    @Test
    void deleteStockItem() {
    }
}