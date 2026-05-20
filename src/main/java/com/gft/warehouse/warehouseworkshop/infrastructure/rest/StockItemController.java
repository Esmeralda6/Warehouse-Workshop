package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.application.service.stockItem.StockItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
@Slf4j
public class StockItemController {

    @Autowired
    private StockItemService stockItemService;

    @GetMapping("")
    public List<StockItemDTO> getStockItems(){
        return stockItemService.getStockItems();
    }

    @GetMapping("/{id}")
    public Optional<StockItemDTO> getStockItemById(
            @PathVariable String id
    ){
        return stockItemService.getStockItemById(id) ;
    }

    @PostMapping("")
    public String saveStockItem(
            @RequestBody StockItemDTO stockItemDTO
    ){
        return stockItemService.saveStockItem( stockItemDTO );
    }

    @PutMapping("/{id}")
    public String updateStockItem(
            @PathVariable String id,
            @RequestBody StockItemDTO stockItemDTO
    ){
        return stockItemService.updateStockItem( id, stockItemDTO );
    }

    @DeleteMapping("/{id}")
    public String deleteStockItem(
            @PathVariable String id
    ){
        return stockItemService.deleteStockItem( id );
    }

}
