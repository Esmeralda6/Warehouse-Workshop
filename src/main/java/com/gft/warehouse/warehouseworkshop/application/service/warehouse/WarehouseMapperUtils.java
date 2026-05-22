package com.gft.warehouse.warehouseworkshop.application.service.warehouse;

import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.application.service.stockItem.StockItemMapperUtils;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.ProductJpaRepository;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.WarehouseJpaRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class WarehouseMapperUtils {

    @lombok.Generated
    protected WarehouseMapperUtils() {}

    public static WarehouseDTO toDTO(Warehouse warehouse){

        return WarehouseDTO.builder()
                .id( warehouse.getWarehouseId().getId().toString())
                .name( warehouse.getWarehouseName() )
                .type( warehouse.getWarehouseType().name() )
                .location(
                        LocationDTO.builder()
                                .x( warehouse.getWarehouseLocation().getX() )
                                .y( warehouse.getWarehouseLocation().getY() )
                                .build())
                .factoryId( nullableIdToString(warehouse.getFactoryId().getId()) )
                .build();
    }

    public static WarehouseDTO toDto(WarehouseEntity warehouseEntity){

        return WarehouseDTO.builder()
                .id( warehouseEntity.getId().toString())
                .name( warehouseEntity.getName() )
                .type( warehouseEntity.getWarehouseType().name() )
                .location(
                        LocationDTO.builder()
                                .x( warehouseEntity.getX() )
                                .y( warehouseEntity.getY() )
                                .build())
                .factoryId( nullableIdToString( warehouseEntity.getFactoryId()) )
                .build();
    }

    public static Warehouse toDomain(WarehouseDTO warehouseDTO){
        return Warehouse.builder()
                .warehouseId(
                        WarehouseId.builder()
                                .id( UUID.fromString( warehouseDTO.getId() ) )
                                .build()
                )
                .warehouseName( warehouseDTO.getName() )
                .warehouseType( WarehouseType.valueOf( warehouseDTO.getType().toUpperCase() ) )
                .warehouseLocation(
                        Location.builder()
                                .x(warehouseDTO.getLocation().getX())
                                .y(warehouseDTO.getLocation().getY())
                                .build()
                )
                .factoryId(
                        FactoryId.builder()
                                .id( nullableIdToUuid( warehouseDTO.getFactoryId() ))
                                .build() )
                .build();
    }

    public static Warehouse toDomain(WarehouseEntity entity){
        return Warehouse.builder()
                .warehouseId(WarehouseId.builder().id( entity.getId() ).build())
                .warehouseName( entity.getName() )
                .warehouseType( entity.getWarehouseType() )
                .warehouseLocation(
                        Location.builder()
                                .x(entity.getX())
                                .y(entity.getY())
                                .build()
                )
                .factoryId(
                        FactoryId.builder()
                                .id( entity.getFactoryId() )
                                .build()
                )
                .stock(
                        entity.getStockItems().stream().map(StockItemMapperUtils::toDomain).toList()
                )
                .build();
    }

    //Without parsing stock items
    public static WarehouseEntity toEntity(Warehouse warehouse){
        return WarehouseEntity.builder()
                .id( warehouse.getWarehouseId().getId() )
                .name( warehouse.getWarehouseName() )
                .warehouseType( warehouse.getWarehouseType())
                .x( warehouse.getWarehouseLocation().getX() )
                .y( warehouse.getWarehouseLocation().getY() )
                .factoryId( warehouse.getFactoryId().getId() )
                .build();
    }

    //Properly parsing stock items
    public static WarehouseEntity toEntity(Warehouse warehouse, WarehouseJpaRepository warehouseJpaRepository, ProductJpaRepository productJpaRepository){
        return WarehouseEntity.builder()
                .id( warehouse.getWarehouseId().getId() )
                .name( warehouse.getWarehouseName() )
                .warehouseType( warehouse.getWarehouseType())
                .x( warehouse.getWarehouseLocation().getX() )
                .y( warehouse.getWarehouseLocation().getY() )
                .factoryId( warehouse.getFactoryId().getId() )
                .stockItems(
                        warehouse.getStock().stream().map( stockItem -> StockItemMapperUtils.toEntity(stockItem, productJpaRepository, warehouseJpaRepository) ).toList()
                )
                .build();
    }

    private static String nullableIdToString(UUID id ){
        return id == null ?
                null
                : id.toString();
    }
    private static UUID nullableIdToUuid(String id ){
        return id == null ?
                null
                : UUID.fromString(id);
    }

}
