package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.application.service.GeneralMapperUtils;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.repository.WarehouseRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
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
                .map(GeneralMapperUtils::toDomain)
                .toList();
    }

    @Override
    public Optional<Warehouse> findById(WarehouseId warehouseId) {
        return warehouseJpaRepository.findById( warehouseId.getId() )
                .map(GeneralMapperUtils::toDomain);
    }

    @Override
    public WarehouseEntity save(Warehouse warehouse) {
        return warehouseJpaRepository.save(
                GeneralMapperUtils.toEntity( warehouse )
        );
    }

    @Override
    public void delete( WarehouseId warehouseId){
        warehouseJpaRepository.deleteById(
                warehouseId.getId()
        );
    }

    @Override
    public Optional<Warehouse> findAvailable() {
        return warehouseJpaRepository
                .findByWarehouseTypeAndFactoryIdNull(
                    WarehouseType.FACTORY
                )
                .stream()
                .findFirst()
                .map(GeneralMapperUtils::toDomain);
    }

    @Override
    public boolean assignFactory(WarehouseId warehouseId, FactoryId factoryId) {
        return warehouseJpaRepository.assignFactoryIdIfAvailable(
                warehouseId.getId(), factoryId.getId()) > 0;
    }
}
