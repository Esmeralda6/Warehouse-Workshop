package com.gft.warehouse.warehouseworkshop.application.service.stockItem;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.*;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class StockItemMapperUtils {
    public static StockItemDTO toDTO(StockItem stockItem){

        return StockItemDTO.builder()
                .id( stockItem.getStockItemId().getId().toString() )
                .productId( stockItem.getProductId().getId().toString())
                .quantity( stockItem.getQuantity().getValue() )
                .warehouseId( stockItem.getWarehouseId().getId().toString() )
                .minimumQuantity( stockItem.getMinimumQuantityRule().getValue() )
                .build();
    }

    public static StockItemDTO toDTO(StockItemEntity stockItemEntity){

        return StockItemDTO.builder()
                .id( stockItemEntity.getId().toString() )
                .productId( stockItemEntity.getProductId().toString())
                .quantity( stockItemEntity.getQuantity() )
                .warehouseId( stockItemEntity.getWarehouseId().toString() )
                .minimumQuantity( stockItemEntity.getMinimumQuantity() )
                .build();
    }

    public static StockItem toDomain(StockItemDTO stockItemDTO){
        UUID stockItemid = stockItemDTO.getId() != null ?
                UUID.fromString(stockItemDTO.getId())
                : UUID.randomUUID();
        return StockItem.builder()
                .stockItemId(
                        StockItemId.builder()
                                .id( stockItemid )
                                .build()
                )
                .productId(
                        ProductId.builder()
                                .id(UUID.fromString(stockItemDTO.getProductId()))
                                .build()
                )
                .quantity(
                        Quantity.builder()
                            .value(stockItemDTO.getQuantity())
                            .build()
                )

                .warehouseId(
                        WarehouseId.builder()
                                .id( UUID.fromString( stockItemDTO.getWarehouseId() ) )
                                .build()
                )
                .minimumQuantityRule(
                        Quantity.builder()
                                .value(stockItemDTO.getMinimumQuantity())
                                .build()

                )
                .build();
    }

    public static StockItem toDomain(StockItemEntity entity){
        return StockItem.builder()
                .stockItemId(
                        StockItemId.builder()
                                .id( entity.getId() )
                                .build()
                )
                .productId(
                        ProductId.builder()
                                .id( entity.getProductId() )
                                .build()
                )
                .quantity(
                        Quantity.builder()
                                .value(entity.getQuantity() )
                                .build()
                )

                .warehouseId(
                        WarehouseId.builder()
                                .id( entity.getWarehouseId().getId() )
                                .build()
                )
                .minimumQuantityRule(
                        Quantity.builder()
                                .value( entity.getMinimumQuantity() )
                                .build()

                )
                .build();
    }

    public static StockItemEntity toEntity(StockItem stockItem){
        return StockItemEntity.builder()
                .id( stockItem.getStockItemId().getId() )
                .productId( stockItem.getStockItemId().getId() )
                .quantity( stockItem.getQuantity().getValue() )
                .warehouseId( stockItem.getWarehouseId().getId() )
                .minimumQuantity( stockItem.getMinimumQuantityRule().getValue() )
                .build();
    }

}
