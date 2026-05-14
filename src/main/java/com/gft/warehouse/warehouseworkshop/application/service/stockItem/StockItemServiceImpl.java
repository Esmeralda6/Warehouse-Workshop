package com.gft.warehouse.warehouseworkshop.application.service.stockItem;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.application.service.warehouse.WarehouseMapperUtils;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.repository.StockItemRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.*;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class StockItemServiceImpl implements StockItemService{

    @Autowired
    private StockItemRepository stockItemRepository;

    // GET StockItems list
    @Override
    public List<StockItemDTO> getStockItems(){
        return stockItemRepository.findAll()
                .stream()
                .map(StockItemMapperUtils::toDTO)
                .toList();
    }

    @Override
    public Optional<StockItemDTO> getStockItemById(String id) {
        return stockItemRepository.findById(StockItemId.builder().id( UUID.fromString( id ) ).build() )
                .map(StockItemMapperUtils::toDTO);
    }

    @Override
    public String saveStockItem(StockItemDTO stockItemDTO) {

        if ( stockItemDTO.getId() == null || stockItemDTO.getId().isBlank() )
            stockItemDTO = StockItemDTO.builder()
                    .id( UUID.randomUUID().toString() )
                    .warehouseId(stockItemDTO.getWarehouseId())
                    .productId( stockItemDTO.getProductId() )
                    .quantity( stockItemDTO.getQuantity())
                    .minimumQuantity( stockItemDTO.getMinimumQuantity() )
                    .build();

        log.info( stockItemDTO.toString() );
        StockItem stockItem = StockItemMapperUtils.toDomain(stockItemDTO);
        log.info( stockItem.toString() );

        StockItemEntity savedStockItem = stockItemRepository.save(
                stockItem
        );
        log.info( savedStockItem.toString() );
        return "Stock Item saved with id: " + savedStockItem.getId().toString();
    }

    @Override
    public String updateStockItem(String id, StockItemDTO warehouseDTO) {
        Optional<StockItemDTO> stockItem = stockItemRepository.findById(
                        StockItemId.builder().id(UUID.fromString(id)).build())
                .map(updatedStockItem -> {
                    updatedStockItem.setQuantity(
                            Quantity.builder().value(warehouseDTO.getQuantity()).build()
                    );
                    updatedStockItem.setMinimumQuantityRule(
                            Quantity.builder().value(warehouseDTO.getMinimumQuantity()).build()
                    );
                    return StockItemMapperUtils.toDTO( stockItemRepository.save( updatedStockItem ));
                });
        if (stockItem.isPresent()){
            return "Stock item with id " + id + " succesfully updated";
        }
        return "Stock item with id " + id + " not found.";
    }

    @Override
    public String deleteStockItem(String id) {
        StockItemId stockItemId = StockItemId.builder().id(UUID.fromString(id)).build();
        if(
                stockItemRepository.findById( stockItemId ).isPresent()
        ){
            stockItemRepository.delete( stockItemId );
            return "Stock item with id " + id + " succesfully deleted.";
        }
        return "Stock item with id " + id + " was not found.";
    }

}
