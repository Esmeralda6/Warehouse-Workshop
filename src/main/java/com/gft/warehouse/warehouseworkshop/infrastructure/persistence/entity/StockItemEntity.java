package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "stockItems", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "warehouse_id"})
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StockItemEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productId;

    @Column(name="quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private WarehouseEntity warehouseId;

    @Column(name="minimum_quantity")
    private int minimumQuantity;


}
