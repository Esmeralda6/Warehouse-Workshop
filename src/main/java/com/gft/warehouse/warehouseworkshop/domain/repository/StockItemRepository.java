package com.gft.warehouse.warehouseworkshop.domain.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.StockItemId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;

import java.util.List;
import java.util.Optional;

public interface StockItemRepository {
    List<StockItem> findAll();
    Optional<StockItem> findById(StockItemId stockItemId);
    StockItemEntity save(StockItem stockItem );
    void delete( StockItemId stockItemId);
}
