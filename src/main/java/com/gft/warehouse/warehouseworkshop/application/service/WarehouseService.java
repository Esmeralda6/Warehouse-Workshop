package com.gft.warehouse.warehouseworkshop.application.service;

import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    List<WarehouseDTO> getWarehouses();
    Optional<WarehouseDTO> getWarehouseById(String id);
    String saveWarehouse( WarehouseDTO warehouseDTO );
    String updateWarehouse( String id, WarehouseDTO warehouseDTO);
    String deleteWarehouse( String id );

    String findAvailableWarehouse(FactoryId factoryId);
}
