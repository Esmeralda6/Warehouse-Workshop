package com.gft.warehouse.warehouseworkshop.application.service;

import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class Mapper {
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
                .factoryId( getNullableIdToString(warehouse.getFactoryId().getId()) )
                .build();
    }

    public static WarehouseDTO toDto(WarehouseEntity warehouseEntity){

        return WarehouseDTO.builder()
                .id( warehouseEntity.getId().toString())
                .name( warehouseEntity.getName() )
                .type( warehouseEntity.getType().name() )
                .location(
                        LocationDTO.builder()
                                .x( warehouseEntity.getX() )
                                .y( warehouseEntity.getY() )
                                .build())
                .factoryId( getNullableIdToString( warehouseEntity.getFactoryId()) )
                .build();
    }

    public static Warehouse toDomain(WarehouseDTO warehouseDTO){
        UUID warehouseId = warehouseDTO.getId() != null ?
                UUID.fromString(warehouseDTO.getId())
                : UUID.randomUUID();
        log.info(warehouseDTO.toString());
        return Warehouse.builder()
                .warehouseId(
                        WarehouseId.builder()
                                .id(warehouseId)
                                .build()
                )
                .warehouseName( warehouseDTO.getName() )
                .warehouseType( Type.valueOf( warehouseDTO.getType().toUpperCase() ) )
                .warehouseLocation(
                        Location.builder()
                                .x(warehouseDTO.getLocation().getX())
                                .y(warehouseDTO.getLocation().getY())
                                .build()
                )
                .factoryId(
                        FactoryId.builder()
                                .id(getNullableIdToUuid(warehouseDTO.getId()))
                                .build() )
                .build();
    }

    //Repository
    public static WarehouseEntity toEntity(Warehouse warehouse){
        return WarehouseEntity.builder()
                .id( warehouse.getWarehouseId().getId() )
                .name( warehouse.getWarehouseName() )
                .type( warehouse.getWarehouseType())
                .x( warehouse.getWarehouseLocation().getX() )
                .y( warehouse.getWarehouseLocation().getY() )
                .factoryId( getNullableIdToUuid(warehouse.getFactoryId().getId()))
                .build();
    }


    public static Warehouse toDomain(WarehouseEntity entity){
        return Warehouse.builder()
                .warehouseId(WarehouseId.builder().id( entity.getId() ).build())
                .warehouseName( entity.getName() )
                .warehouseType( entity.getType() )
                .warehouseLocation(
                        Location.builder()
                                .x(entity.getX())
                                .y(entity.getY())
                                .build()
                )
                .factoryId(
                        FactoryId.builder()
                                .id(getNullableIdToUuid(entity.getFactoryId()))
                                .build()
                )
                .build();
    }

    private static String getNullableIdToString( UUID id ){
        return id ==null ?
                null
                : id.toString();
    }
    private static UUID getNullableIdToUuid( String id ){
        return id ==null ?
                null
                : UUID.fromString(id);
    }
    private static UUID getNullableIdToUuid( UUID id ){
        return id ==null ?
                null
                : id;
    }
}
