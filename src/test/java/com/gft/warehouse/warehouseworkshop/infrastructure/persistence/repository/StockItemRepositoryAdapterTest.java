package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.*;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockItemRepositoryAdapterTest {

    @InjectMocks
    private StockItemRepositoryAdapter stockItemRepository;

    @Mock
    private StockItemJpaRepository stockItemJpaRepository;

    @Mock
    private WarehouseJpaRepository warehouseJpaRepository;

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Test
    void findAll() {
        UUID productId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        when(stockItemJpaRepository.findAll()).thenReturn(
                List.of(StockItemEntity.builder()
                        .id(UUID.randomUUID())
                        .productId(ProductEntity.builder().id(productId).build())
                        .warehouseId(WarehouseEntity.builder().id(warehouseId).build())
                        .quantity(10)
                        .minimumQuantity(2)
                        .build())
        );

        var result = stockItemRepository.findAll();

        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.getFirst())
                .isInstanceOf(StockItem.class);
    }

    @Test
    void findById() {
        StockItemId id = StockItemId.builder().id(UUID.randomUUID()).build();
        UUID productId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        when(stockItemJpaRepository.findById(id.getId())).thenReturn(
                Optional.of(StockItemEntity.builder()
                        .id(id.getId())
                        .productId(ProductEntity.builder().id(productId).build())
                        .warehouseId(WarehouseEntity.builder().id(warehouseId).build())
                        .quantity(5)
                        .minimumQuantity(1)
                        .build())
        );

        var result = stockItemRepository.findById(id);

        assertThat(result)
                .isNotNull();
        assertThat(result.isPresent())
                .isTrue();
        assertThat(result.get())
                .isInstanceOf(StockItem.class);
        assertThat(result.get().getStockItemId())
                .isEqualTo(id);
    }

    @Test
    void save() {
        UUID productUUID = UUID.randomUUID();
        UUID warehouseUUID = UUID.randomUUID();
        UUID stockItemUUID = UUID.randomUUID();

        StockItem stockItem = StockItem.builder()
                .stockItemId(StockItemId.builder().id(stockItemUUID).build())
                .productId(ProductId.builder().id(productUUID).build())
                .warehouseId(WarehouseId.builder().id(warehouseUUID).build())
                .quantity(Quantity.builder().value(10).build())
                .minimumQuantityRule(Quantity.builder().value(2).build())
                .build();

        ProductEntity productEntity = ProductEntity.builder().id(productUUID).build();
        WarehouseEntity warehouseEntity = WarehouseEntity.builder().id(warehouseUUID).build();

        when(productJpaRepository.findById(productUUID)).thenReturn(Optional.of(productEntity));
        when(warehouseJpaRepository.findById(warehouseUUID)).thenReturn(Optional.of(warehouseEntity));
        when(stockItemJpaRepository.save(any(StockItemEntity.class))).thenReturn(
                StockItemEntity.builder()
                        .id(stockItemUUID)
                        .productId(productEntity)
                        .warehouseId(warehouseEntity)
                        .quantity(10)
                        .minimumQuantity(2)
                        .build()
        );

        assertThatNoException().isThrownBy(() -> stockItemRepository.save(stockItem));
        verify(stockItemJpaRepository).save(any(StockItemEntity.class));
    }

    @Test
    void delete() {
        StockItemId id = StockItemId.builder().id(UUID.randomUUID()).build();

        assertThatNoException().isThrownBy(() -> stockItemRepository.delete(id));
        verify(stockItemJpaRepository).deleteById(id.getId());
    }
}
