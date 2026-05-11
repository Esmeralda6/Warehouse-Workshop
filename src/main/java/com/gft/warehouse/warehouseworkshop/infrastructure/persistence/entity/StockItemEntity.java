package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "stockItems")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StockItemEntity {

    @Id
    private UUID id;

    @Column(name="warehouse_id")
    private UUID warehouseId;

    @Column(name="product_id")
    private UUID productId;

    @Column(name="quantity")
    private int quantity;
    @Column(name="minimum_quantity")
    private int minimumQuantity;


}
