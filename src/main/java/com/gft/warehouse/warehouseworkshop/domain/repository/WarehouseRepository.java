package com.gft.warehouse.warehouseworkshop.domain.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository {
    List<Warehouse> findAll();
    Optional<Warehouse> findById(WarehouseId warehouseId);
    void save( Warehouse warehouse );
}
