package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface WarehouseJpaRepository extends JpaRepository<WarehouseEntity, UUID> {

    List<WarehouseEntity> findByWarehouseTypeAndFactoryIdNull(WarehouseType warehouseType);

    // Returns 1 if the row was claimed, 0 if not found or already assigned.
    @Modifying
    @Transactional
    @Query("UPDATE WarehouseEntity w SET w.factoryId = :factoryId WHERE w.id = :id AND w.factoryId IS NULL")
    int assignFactoryIdIfAvailable(@Param("id") UUID id, @Param("factoryId") UUID factoryId);
}
