package com.gft.warehouse.warehouseworkshop.application.service.warehouse;

import com.gft.warehouse.warehouseworkshop.application.dto.FactoryIdDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    List<WarehouseDTO> getWarehouses();
    Optional<WarehouseDTO> getWarehouseById(String id);
    String saveWarehouse( WarehouseDTO warehouseDTO );
    String updateWarehouse( String id, WarehouseDTO warehouseDTO);
    String deleteWarehouse( String id );

    Optional<WarehouseDTO> findAvailableWarehouse();
    String assignFactoryId( String warehouseId, FactoryIdDTO factoryId);
}
