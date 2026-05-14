package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.application.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    void getProducts() {
        when(productService.getProducts()).thenReturn(
                List.of(ProductDTO.builder().id(UUID.randomUUID().toString()).name("product_1").build())
        );

        var result = productController.getProducts();

        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.getFirst())
                .isInstanceOf(ProductDTO.class);
    }

    @Test
    void getProductById() {
        String id = UUID.randomUUID().toString();
        when(productService.getProductById(id)).thenReturn(
                Optional.of(ProductDTO.builder().id(id).name("product_1").build())
        );

        var result = productController.getProductById(id);

        assertThat(result)
                .isNotNull();
        assertThat(result.isPresent())
                .isTrue();
        assertThat(result.get().getId())
                .isEqualTo(id);
    }

    @Test
    void saveProduct() {
        ProductDTO productDTO = ProductDTO.builder().id(UUID.randomUUID().toString()).name("product_1").build();
        when(productService.saveProduct(productDTO)).thenReturn("Product saved with id: " + productDTO.getId());

        var result = productController.saveProduct(productDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result.substring(result.length() - productDTO.getId().length()))
                .isEqualTo(productDTO.getId());
    }

    @Test
    void updateProduct() {
        String id = UUID.randomUUID().toString();
        ProductDTO productDTO = ProductDTO.builder().name("updated_product").build();
        when(productService.updateProduct(id, productDTO)).thenReturn("Product with id " + id + " succesfully updated");

        var result = productController.updateProduct(id, productDTO);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id);
    }

    @Test
    void deleteStockItem() {
        String id = UUID.randomUUID().toString();
        when(productService.deleteProduct(id)).thenReturn("Product with id " + id + " succesfully deleted.");

        var result = productController.deleteProduct(id);

        assertThat(result)
                .isNotNull()
                .isNotBlank();
        assertThat(result)
                .contains(id);
    }
}
