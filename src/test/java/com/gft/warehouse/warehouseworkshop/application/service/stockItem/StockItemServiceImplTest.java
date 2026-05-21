package com.gft.warehouse.warehouseworkshop.application.service.stockItem;

import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.enums.StockVariationType;
import com.gft.warehouse.warehouseworkshop.domain.ports.EventPublisher;
import com.gft.warehouse.warehouseworkshop.domain.repository.StockItemRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.*;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.StockItemEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockItemServiceImplTest {

    @InjectMocks
    private StockItemServiceImpl stockItemService;

    @Mock
    private StockItemRepository stockItemRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Test
    void getStockItems() {
        when(stockItemRepository.findAll()).thenReturn(
                List.of(
                        StockItem.builder()
                                .stockItemId(StockItemId.builder().id(UUID.randomUUID()).build())
                                .productId(ProductId.builder().id(UUID.randomUUID()).build())
                                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                                .quantity(Quantity.builder().value(10).build())
                                .minimumQuantityRule(Quantity.builder().value(2).build())
                                .build()
                )
        );

        var result = stockItemService.getStockItems();

        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.getFirst())
                .isInstanceOf(StockItemDTO.class);
    }

    @Test
    void getStockItemById() {
        StockItemId id = StockItemId.builder().id(UUID.randomUUID()).build();
        when(stockItemRepository.findById(id)).thenReturn(
                Optional.of(StockItem.builder()
                        .stockItemId(id)
                        .productId(ProductId.builder().id(UUID.randomUUID()).build())
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .quantity(Quantity.builder().value(5).build())
                        .minimumQuantityRule(Quantity.builder().value(1).build())
                        .build())
        );

        var result = stockItemService.getStockItemById(id.getId().toString());

        assertThat(result)
                .isNotNull();
        assertThat(result.isPresent())
                .isTrue();
        assertThat(result.get())
                .isInstanceOf(StockItemDTO.class);
        assertThat(result.get().getId())
                .isEqualTo(id.getId().toString());
    }

    private static Stream<Arguments> providerNullableId() {
        return Stream.of(
                Arguments.of(StockItemDTO.builder()
                        .id(null)
                        .productId(UUID.randomUUID().toString())
                        .warehouseId(UUID.randomUUID().toString())
                        .quantity(10)
                        .minimumQuantity(2)
                        .build()),
                Arguments.of(StockItemDTO.builder()
                        .id("")
                        .productId(UUID.randomUUID().toString())
                        .warehouseId(UUID.randomUUID().toString())
                        .quantity(10)
                        .minimumQuantity(2)
                        .build()),
                Arguments.of(StockItemDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .productId(UUID.randomUUID().toString())
                        .warehouseId(UUID.randomUUID().toString())
                        .quantity(10)
                        .minimumQuantity(2)
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("providerNullableId")
    void saveStockItem(StockItemDTO stockItemDTO) {
        when(stockItemRepository.save(Mockito.any(StockItem.class))).thenReturn(
                StockItemEntity.builder()
                        .id(UUID.randomUUID())
                        .quantity(stockItemDTO.getQuantity())
                        .minimumQuantity(stockItemDTO.getMinimumQuantity())
                        .build()
        );

        var result = stockItemService.saveStockItem(stockItemDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .doesNotContain("null");
    }

    @Test
    void updateStockItemWhenFound() {
        String id = UUID.randomUUID().toString();
        StockItemDTO stockItemDTO = StockItemDTO.builder()
                .quantity(20)
                .minimumQuantity(5)
                .productId(UUID.randomUUID().toString())
                .warehouseId(UUID.randomUUID().toString())
                .build();

        when(stockItemRepository.findById(StockItemId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.of(StockItem.builder()
                        .stockItemId(StockItemId.builder().id(UUID.fromString(id)).build())
                        .productId(ProductId.builder().id(UUID.randomUUID()).build())
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .quantity(Quantity.builder().value(10).build())
                        .minimumQuantityRule(Quantity.builder().value(2).build())
                        .build())
        );
        when(stockItemRepository.save(Mockito.any(StockItem.class))).thenReturn(
                StockItemEntity.builder()
                        .id(UUID.fromString(id))
                        .productId(
                                ProductEntity.builder().id(UUID.randomUUID()).build()
                        )
                        .warehouseId(
                                WarehouseEntity.builder().id(UUID.randomUUID()).build()
                        )
                        .quantity(stockItemDTO.getQuantity())
                        .minimumQuantity(stockItemDTO.getMinimumQuantity())
                        .build()
        );

        var result = stockItemService.updateStockItem(id, stockItemDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id + " succesfully updated");
    }

    @Test
    void updateStockItemWhenNotFound() {
        String id = UUID.randomUUID().toString();
        StockItemDTO stockItemDTO = StockItemDTO.builder()
                .quantity(20)
                .minimumQuantity(5)
                .productId(UUID.randomUUID().toString())
                .warehouseId(UUID.randomUUID().toString())
                .build();

        when(stockItemRepository.findById(StockItemId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.empty()
        );

        var result = stockItemService.updateStockItem(id, stockItemDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id + " not found.");
    }

    @Test
    void deleteStockItemWhenFound() {
        String id = UUID.randomUUID().toString();

        when(stockItemRepository.findById(StockItemId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.of(StockItem.builder()
                        .stockItemId(StockItemId.builder().id(UUID.fromString(id)).build())
                        .productId(ProductId.builder().id(UUID.randomUUID()).build())
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .quantity(Quantity.builder().value(10).build())
                        .minimumQuantityRule(Quantity.builder().value(2).build())
                        .build())
        );

        var result = stockItemService.deleteStockItem(id);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id + " succesfully deleted.");
    }

    @Test
    void deleteStockItemWhenNotFound() {
        String id = UUID.randomUUID().toString();

        when(stockItemRepository.findById(StockItemId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.empty()
        );

        var result = stockItemService.deleteStockItem(id);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id + " was not found.");
    }

    @Test
    void saveStockItem_whenPublisherThrows_catchesExceptionAndReturnsId() throws Exception {
        StockItemDTO dto = StockItemDTO.builder()
                .id(UUID.randomUUID().toString())
                .productId(UUID.randomUUID().toString())
                .warehouseId(UUID.randomUUID().toString())
                .quantity(10)
                .minimumQuantity(2)
                .build();
        when(stockItemRepository.save(any(StockItem.class))).thenReturn(
                StockItemEntity.builder().id(UUID.randomUUID()).quantity(10).minimumQuantity(2).build()
        );
        doThrow(new RuntimeException("publisher error")).when(eventPublisher).stockChanged(any(), any());

        assertThatCode(() -> stockItemService.saveStockItem(dto)).doesNotThrowAnyException();
    }

    @Test
    void updateStockItem_whenSavedQuantityIsHigherThanInput_usesIncreaseVariationType() {
        String id = UUID.randomUUID().toString();
        StockItemDTO stockItemDTO = StockItemDTO.builder()
                .quantity(5)
                .minimumQuantity(2)
                .productId(UUID.randomUUID().toString())
                .warehouseId(UUID.randomUUID().toString())
                .build();

        when(stockItemRepository.findById(StockItemId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.of(StockItem.builder()
                        .stockItemId(StockItemId.builder().id(UUID.fromString(id)).build())
                        .productId(ProductId.builder().id(UUID.randomUUID()).build())
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .quantity(Quantity.builder().value(2).build())
                        .minimumQuantityRule(Quantity.builder().value(1).build())
                        .build())
        );
        // entity returns quantity 20 > input quantity 5 → INCREASE branch
        when(stockItemRepository.save(any(StockItem.class))).thenReturn(
                StockItemEntity.builder()
                        .id(UUID.fromString(id))
                        .productId(ProductEntity.builder().id(UUID.randomUUID()).build())
                        .warehouseId(WarehouseEntity.builder().id(UUID.randomUUID()).build())
                        .quantity(20)
                        .minimumQuantity(stockItemDTO.getMinimumQuantity())
                        .build()
        );

        var result = stockItemService.updateStockItem(id, stockItemDTO);

        assertThat(result).contains(id + " succesfully updated");
    }

    @Test
    void updateStockItem_whenPublisherThrows_catchesExceptionAndReturnsSuccess() throws Exception {
        String id = UUID.randomUUID().toString();
        StockItemDTO stockItemDTO = StockItemDTO.builder()
                .quantity(20)
                .minimumQuantity(5)
                .productId(UUID.randomUUID().toString())
                .warehouseId(UUID.randomUUID().toString())
                .build();

        when(stockItemRepository.findById(StockItemId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.of(StockItem.builder()
                        .stockItemId(StockItemId.builder().id(UUID.fromString(id)).build())
                        .productId(ProductId.builder().id(UUID.randomUUID()).build())
                        .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                        .quantity(Quantity.builder().value(10).build())
                        .minimumQuantityRule(Quantity.builder().value(2).build())
                        .build())
        );
        when(stockItemRepository.save(any(StockItem.class))).thenReturn(
                StockItemEntity.builder()
                        .id(UUID.fromString(id))
                        .productId(ProductEntity.builder().id(UUID.randomUUID()).build())
                        .warehouseId(WarehouseEntity.builder().id(UUID.randomUUID()).build())
                        .quantity(stockItemDTO.getQuantity())
                        .minimumQuantity(stockItemDTO.getMinimumQuantity())
                        .build()
        );
        doThrow(new RuntimeException("publisher error")).when(eventPublisher).stockChanged(any(), any());

        assertThatCode(() -> stockItemService.updateStockItem(id, stockItemDTO)).doesNotThrowAnyException();
    }
}
