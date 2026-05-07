package com.gft.warehouse.warehouseworkshop.domain.services;

import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;

public interface SupplierWarehouseFinder {
    WarehouseId findSupplier(String factoryRecipe);
}
