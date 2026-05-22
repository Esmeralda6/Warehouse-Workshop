package com.gft.warehouse.warehouseworkshop.application.service.stockItem;

import com.gft.warehouse.warehouseworkshop.application.dto.ItemRequestDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.ShipmentRequestDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.enums.StockVariationType;
import com.gft.warehouse.warehouseworkshop.domain.ports.EventPublisher;
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

    @Autowired
    private EventPublisher eventPublisher;

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

        StockItem stockItem = StockItemMapperUtils.toDomain(stockItemDTO);

        StockItemEntity savedStockItem = stockItemRepository.save(
                stockItem
        );

        try {
            eventPublisher.stockChanged(stockItem, StockVariationType.INCREASE);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return "Stock Item saved with id: " + savedStockItem.getId().toString();
    }

    @Override
    public String updateStockItem(String id, StockItemDTO warehouseDTO) {
        Optional<StockItem> oldStockItem = stockItemRepository.findById(
                StockItemId.builder().id(UUID.fromString(id)).build());
        Optional<StockItemDTO> stockItemDTOOptional = oldStockItem
                .map(stockItem -> {
                    stockItem.setQuantity(
                            Quantity.builder().value(warehouseDTO.getQuantity()).build()
                    );
                    stockItem.setMinimumQuantityRule(
                            Quantity.builder().value(warehouseDTO.getMinimumQuantity()).build()
                    );
                    return StockItemMapperUtils.toDTO( stockItemRepository.save(stockItem));
                });
        if (stockItemDTOOptional.isPresent()){
            try {
                StockItem updatedStockItem = StockItemMapperUtils.toDomain(stockItemDTOOptional.get());
                StockVariationType variationType =
                        updatedStockItem.getQuantity().getValue() > oldStockItem.get().getQuantity().getValue() ?
                        StockVariationType.INCREASE : StockVariationType.DECREASE;
                eventPublisher.stockChanged(
                        updatedStockItem
                        , variationType);
            }catch (Exception e){
                log.error(e.getMessage());
            }

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
