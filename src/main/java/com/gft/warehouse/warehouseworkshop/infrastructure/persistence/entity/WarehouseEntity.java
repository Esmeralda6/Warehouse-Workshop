package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity;

import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "warehouses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WarehouseEntity {

    @Id
    private UUID id;

    @Column(name="name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private WarehouseType warehouseType;

    //Location
    @Column(name="location_x")
    private int x;
    @Column(name="location_y")
    private int y;

    @Column(name = "factory_id")
    private UUID factoryId;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockItemEntity> stockItems = new ArrayList<>();
}
