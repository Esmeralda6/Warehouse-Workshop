package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockItemJpaRepository extends JpaRepository<StockItemEntity, UUID> {
}