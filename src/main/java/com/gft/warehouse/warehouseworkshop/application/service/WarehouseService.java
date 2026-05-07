package com.gft.warehouse.warehouseworkshop.application.service;

import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    List<WarehouseDTO> getWarehouses();
    Optional<WarehouseDTO> getWarehouseById(String id);
    String saveWarehouse( WarehouseDTO warehouseDTO );
}
