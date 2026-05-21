package com.gft.warehouse.warehouseworkshop.domain.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository {
    List<Warehouse> findAll();
    Optional<Warehouse> findById(WarehouseId warehouseId);
    WarehouseEntity save(Warehouse warehouse);
    void delete(WarehouseId warehouseId);
    Optional<Warehouse> findAvailable();

    // Atomic claim: returns true only if the row existed and factory_id was null.
    boolean assignFactory(WarehouseId warehouseId, FactoryId factoryId);
}
