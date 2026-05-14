package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.application.service.stockItem.StockItemMapperUtils;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.repository.StockItemRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.StockItemId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

    @Repository
    @RequiredArgsConstructor
public class StockItemRepositoryAdapter implements StockItemRepository {

    @Autowired
    private final StockItemJpaRepository stockItemJpaRepository;

    @Autowired
    private final WarehouseJpaRepository warehouseJpaRepository;

    @Override
    public List<StockItem> findAll() {
        return stockItemJpaRepository.findAll()
                .stream()
                .map(StockItemMapperUtils::toDomain)
                .toList();
    }

    @Override
    public Optional<StockItem> findById(StockItemId stockItemId) {
        return stockItemJpaRepository.findById( stockItemId.getId() )
                .map(StockItemMapperUtils::toDomain);
    }

    @Override
    public StockItemEntity save(StockItem stockItem) {
        return stockItemJpaRepository.save(
                StockItemMapperUtils.toEntity( stockItem, warehouseJpaRepository )
        );
    }

    @Override
    public void delete(StockItemId stockItemId) {
        stockItemJpaRepository.deleteById(
                stockItemId.getId()
        );
    }
}
