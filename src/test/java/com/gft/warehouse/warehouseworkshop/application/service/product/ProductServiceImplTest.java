package com.gft.warehouse.warehouseworkshop.application.service.product;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.domain.repository.ProductRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void getProducts() {
        when(productRepository.findAll()).thenReturn(
                List.of(
                        Product.builder()
                                .productId(ProductId.builder().id(UUID.randomUUID()).build())
                                .productName("product_1")
                                .build()
                )
        );

        var result = productService.getProducts();

        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.getFirst())
                .isInstanceOf(ProductDTO.class);
    }

    @Test
    void getProductById() {
        ProductId id = ProductId.builder().id(UUID.randomUUID()).build();
        when(productRepository.findById(id)).thenReturn(
                Optional.of(Product.builder()
                        .productId(id)
                        .productName("product_1")
                        .build())
        );

        var result = productService.getProductById(id.getId().toString());

        assertThat(result)
                .isNotNull();
        assertThat(result.isPresent())
                .isTrue();
        assertThat(result.get())
                .isInstanceOf(ProductDTO.class);
        assertThat(result.get().getId())
                .isEqualTo(id.getId().toString());
    }

    private static Stream<Arguments> providerNullableId() {
        return Stream.of(
                Arguments.of(ProductDTO.builder()
                        .id(null)
                        .name("product_1")
                        .build()),
                Arguments.of(ProductDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .name("product_1")
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("providerNullableId")
    void saveProduct(ProductDTO productDTO) {
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(
                ProductEntity.builder()
                        .id(UUID.randomUUID())
                        .productName(productDTO.getName())
                        .build()
        );

        var result = productService.saveProduct(productDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .doesNotContain("null");
    }

    @Test
    void updateProductWhenFound() {
        String id = UUID.randomUUID().toString();
        ProductDTO productDTO = ProductDTO.builder().name("updated_product").build();

        when(productRepository.findById(ProductId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.of(Product.builder()
                        .productId(ProductId.builder().id(UUID.fromString(id)).build())
                        .productName("product_1")
                        .build())
        );
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(
                ProductEntity.builder()
                        .id(UUID.fromString(id))
                        .productName(productDTO.getName())
                        .build()
        );

        var result = productService.updateProduct(id, productDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id + " succesfully updated");
    }

    @Test
    void updateProductWhenNotFound() {
        String id = UUID.randomUUID().toString();
        ProductDTO productDTO = ProductDTO.builder().name("updated_product").build();

        when(productRepository.findById(ProductId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.empty()
        );

        var result = productService.updateProduct(id, productDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id + " not found.");
    }

    @Test
    void deleteProductWhenFound() {
        String id = UUID.randomUUID().toString();

        when(productRepository.findById(ProductId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.of(Product.builder()
                        .productId(ProductId.builder().id(UUID.fromString(id)).build())
                        .build())
        );

        var result = productService.deleteProduct(id);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id + " succesfully deleted.");
    }

    @Test
    void deleteProductWhenNotFound() {
        String id = UUID.randomUUID().toString();

        when(productRepository.findById(ProductId.builder().id(UUID.fromString(id)).build())).thenReturn(
                Optional.empty()
        );

        var result = productService.deleteProduct(id);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id + " was not found.");
    }
}