package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
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
class ProductRepositoryAdapterTest {

    @InjectMocks
    private ProductRepositoryAdapter productRepository;

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Test
    void findAll() {
        when(productJpaRepository.findAll()).thenReturn(
                List.of(ProductEntity.builder().id(UUID.randomUUID()).productName("product_1").build())
        );

        var result = productRepository.findAll();

        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.getFirst())
                .isInstanceOf(Product.class);
    }

    @Test
    void findById() {
        ProductId id = ProductId.builder().id(UUID.randomUUID()).build();
        when(productJpaRepository.findById(id.getId())).thenReturn(
                Optional.of(ProductEntity.builder().id(id.getId()).productName("product_1").build())
        );

        var result = productRepository.findById(id);

        assertThat(result)
                .isNotNull();
        assertThat(result.isPresent())
                .isTrue();
        assertThat(result.get())
                .isInstanceOf(Product.class);
        assertThat(result.get().getProductId())
                .isEqualTo(id);
    }

    @Test
    void save() {
        Product product = Product.builder()
                .productId(ProductId.builder().id(UUID.randomUUID()).build())
                .productName("product_1")
                .build();

        when(productJpaRepository.save(any(ProductEntity.class))).thenReturn(
                ProductEntity.builder().id(product.getProductId().getId()).productName(product.getProductName()).build()
        );

        assertThatNoException().isThrownBy(() -> productRepository.save(product));
        verify(productJpaRepository).save(any(ProductEntity.class));
    }

    @Test
    void delete() {
        ProductId id = ProductId.builder().id(UUID.randomUUID()).build();

        assertThatNoException().isThrownBy(() -> productRepository.delete(id));
        verify(productJpaRepository).deleteById(id.getId());
    }
}
