package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.repository.WarehouseRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WarehouseRepositoryAdapter implements WarehouseRepository {

    private final WarehouseJpaRepository warehouseJpaRepository;


    @Override
    public List<Warehouse> findAll() {
        return warehouseJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Warehouse> findById(WarehouseId warehouseId) {
        return warehouseJpaRepository.findById( warehouseId.getId() )
                .map(this::toDomain);
    }

    @Override
    public void save(Warehouse warehouse) {
        warehouseJpaRepository.save(
                toEntity( warehouse )
        );
    }

    //MAPPERS

    private WarehouseEntity toEntity(Warehouse warehouse){
        return WarehouseEntity.builder()
                .id( warehouse.getWarehouseId().getId() )
                .name( warehouse.getWarehouseName() )
                .x( warehouse.getWarehouseLocation().getX() )
                .y( warehouse.getWarehouseLocation().getY() )
                .type( warehouse.getWarehouseType())
                .build();
    }


    private Warehouse toDomain(WarehouseEntity entity){
        return Warehouse.builder()
                .warehouseId(WarehouseId.builder().id( entity.getId() ).build())
                .warehouseName( entity.getName() )
                .warehouseLocation(
                        Location.builder()
                                .x(entity.getX())
                                .y(entity.getY())
                                .build()
                )
                .warehouseType( entity.getType() )
                .build();
    }
}
