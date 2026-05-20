package com.gft.warehouse.warehouseworkshop.application.service.stockItem;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.*;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.ProductJpaRepository;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.WarehouseJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockItemMapperUtilsTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private WarehouseJpaRepository warehouseJpaRepository;

    @Test
    void toDTO_fromDomain() {
        StockItemId id = StockItemId.builder().id(UUID.randomUUID()).build();
        UUID productId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        StockItem stockItem = StockItem.builder()
                .stockItemId(id)
                .productId(ProductId.builder().id(productId).build())
                .warehouseId(WarehouseId.builder().id(warehouseId).build())
                .quantity(Quantity.builder().value(10).build())
                .minimumQuantityRule(Quantity.builder().value(2).build())
                .build();

        StockItemDTO result = StockItemMapperUtils.toDTO(stockItem);

        assertThat(result.getId()).isEqualTo(id.getId().toString());
        assertThat(result.getProductId()).isEqualTo(productId.toString());
        assertThat(result.getWarehouseId()).isEqualTo(warehouseId.toString());
        assertThat(result.getQuantity()).isEqualTo(10);
        assertThat(result.getMinimumQuantity()).isEqualTo(2);
    }

    @Test
    void toDTO_fromEntity() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        StockItemEntity entity = StockItemEntity.builder()
                .id(id)
                .productId(ProductEntity.builder().id(productId).build())
                .warehouseId(WarehouseEntity.builder().id(warehouseId).build())
                .quantity(5)
                .minimumQuantity(1)
                .build();

        StockItemDTO result = StockItemMapperUtils.toDTO(entity);

        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getProductId()).isEqualTo(productId.toString());
        assertThat(result.getWarehouseId()).isEqualTo(warehouseId.toString());
        assertThat(result.getQuantity()).isEqualTo(5);
        assertThat(result.getMinimumQuantity()).isEqualTo(1);
    }

    @Test
    void toDomain_fromDTO() {
        String id = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        String warehouseId = UUID.randomUUID().toString();
        StockItemDTO dto = StockItemDTO.builder()
                .id(id)
                .productId(productId)
                .warehouseId(warehouseId)
                .quantity(8)
                .minimumQuantity(3)
                .build();

        StockItem result = StockItemMapperUtils.toDomain(dto);

        assertThat(result.getStockItemId().getId().toString()).isEqualTo(id);
        assertThat(result.getProductId().getId().toString()).isEqualTo(productId);
        assertThat(result.getWarehouseId().getId().toString()).isEqualTo(warehouseId);
        assertThat(result.getQuantity().getValue()).isEqualTo(8);
        assertThat(result.getMinimumQuantityRule().getValue()).isEqualTo(3);
    }

    @Test
    void toDomain_fromEntity() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        StockItemEntity entity = StockItemEntity.builder()
                .id(id)
                .productId(ProductEntity.builder().id(productId).build())
                .warehouseId(WarehouseEntity.builder().id(warehouseId).build())
                .quantity(7)
                .minimumQuantity(2)
                .build();

        StockItem result = StockItemMapperUtils.toDomain(entity);

        assertThat(result.getStockItemId().getId()).isEqualTo(id);
        assertThat(result.getProductId().getId()).isEqualTo(productId);
        assertThat(result.getWarehouseId().getId()).isEqualTo(warehouseId);
        assertThat(result.getQuantity().getValue()).isEqualTo(7);
        assertThat(result.getMinimumQuantityRule().getValue()).isEqualTo(2);
    }

    @Test
    void toEntity() {
        UUID stockItemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        StockItem stockItem = StockItem.builder()
                .stockItemId(StockItemId.builder().id(stockItemId).build())
                .productId(ProductId.builder().id(productId).build())
                .warehouseId(WarehouseId.builder().id(warehouseId).build())
                .quantity(Quantity.builder().value(10).build())
                .minimumQuantityRule(Quantity.builder().value(2).build())
                .build();

        ProductEntity productEntity = ProductEntity.builder().id(productId).build();
        WarehouseEntity warehouseEntity = WarehouseEntity.builder().id(warehouseId).build();
        when(productJpaRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(warehouseJpaRepository.findById(warehouseId)).thenReturn(Optional.of(warehouseEntity));

        StockItemEntity result = StockItemMapperUtils.toEntity(stockItem, productJpaRepository, warehouseJpaRepository);

        assertThat(result.getId()).isEqualTo(stockItemId);
        assertThat(result.getProductId().getId()).isEqualTo(productId);
        assertThat(result.getWarehouseId().getId()).isEqualTo(warehouseId);
        assertThat(result.getQuantity()).isEqualTo(10);
        assertThat(result.getMinimumQuantity()).isEqualTo(2);
    }
}