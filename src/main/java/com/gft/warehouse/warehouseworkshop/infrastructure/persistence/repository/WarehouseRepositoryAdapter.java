package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.application.service.Mapper;
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
                .map(Mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Warehouse> findById(WarehouseId warehouseId) {
        return warehouseJpaRepository.findById( warehouseId.getId() )
                .map(Mapper::toDomain);
    }

    @Override
    public WarehouseEntity save(Warehouse warehouse) {
        return warehouseJpaRepository.save(
                Mapper.toEntity( warehouse )
        );
    }

    @Override
    public void delete( WarehouseId warehouseId){
        warehouseJpaRepository.deleteById(
                warehouseId.getId()
        );
    }
}
