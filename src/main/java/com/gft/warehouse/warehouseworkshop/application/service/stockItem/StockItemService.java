package com.gft.warehouse.warehouseworkshop.application.service.stockItem;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;

import java.util.List;
import java.util.Optional;

public interface StockItemService {
    List<StockItemDTO> getStockItems();
    Optional<StockItemDTO> getStockItemById(String id);
    String saveStockItem( StockItemDTO warehouseDTO );
    String updateStockItem( String id, StockItemDTO warehouseDTO);
    String deleteStockItem( String id );

}
