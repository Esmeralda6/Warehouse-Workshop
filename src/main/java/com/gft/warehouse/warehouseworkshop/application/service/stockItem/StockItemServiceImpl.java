package com.gft.warehouse.warehouseworkshop.application.service.stockItem;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.domain.repository.StockItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StockItemServiceImpl implements StockItemService{

    @Autowired
    private StockItemRepository stockItemRepository;

    // GET StockItems list
    public List<StockItemDTO> getStockItems(){
        return stockItemRepository.findAll()
                .stream()
                .map(StockItemMapperUtils::toDTO)
                .toList();
    }

}
