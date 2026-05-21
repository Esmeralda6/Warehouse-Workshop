package com.gft.warehouse.warehouseworkshop.application.service.stockItem;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.*;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.ProductJpaRepository;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.WarehouseJpaRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class StockItemMapperUtils {

    @lombok.Generated
    protected StockItemMapperUtils() {}

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
                .productId( stockItemEntity.getProductId().getId().toString())
                .quantity( stockItemEntity.getQuantity() )
                .warehouseId( stockItemEntity.getWarehouseId().getId().toString() )
                .minimumQuantity( stockItemEntity.getMinimumQuantity() )
                .build();
    }

    public static StockItem toDomain(StockItemDTO stockItemDTO){
        return StockItem.builder()
                .stockItemId(
                        StockItemId.builder()
                                .id( UUID.fromString( stockItemDTO.getId() ) )
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
                                .id( entity.getProductId().getId() )
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

    public static StockItemEntity toEntity(StockItem stockItem, ProductJpaRepository productJpaRepository, WarehouseJpaRepository warehouseJpaRepository){
        return StockItemEntity.builder()
                .id( stockItem.getStockItemId().getId() )
                .productId( getEntityById( stockItem.getProductId(), productJpaRepository) )
                .quantity( stockItem.getQuantity().getValue() )
                .warehouseId( getEntityById( stockItem.getWarehouseId(), warehouseJpaRepository ) )
                .minimumQuantity( stockItem.getMinimumQuantityRule().getValue() )
                .build();
    }

    //StockItemEntity holds other Entities, while the domain object StockItem only holds the ids,
    // meaning in order to map the full information of the entity we need to obtain it through its repository
    private static WarehouseEntity getEntityById(WarehouseId warehouseId, WarehouseJpaRepository warehouseJpaRepository ){
        WarehouseEntity warehouseEntity = warehouseJpaRepository.findById( warehouseId.getId() ).orElseThrow();
        return warehouseEntity;
    }
    private static ProductEntity getEntityById(ProductId productId, ProductJpaRepository productJpaRepository){
        ProductEntity productEntity = productJpaRepository.findById( productId.getId() ).orElseThrow();
        return productEntity;
    }
}
