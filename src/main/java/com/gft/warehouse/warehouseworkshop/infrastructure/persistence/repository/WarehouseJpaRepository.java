package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WarehouseJpaRepository extends JpaRepository<WarehouseEntity, UUID> {
}
