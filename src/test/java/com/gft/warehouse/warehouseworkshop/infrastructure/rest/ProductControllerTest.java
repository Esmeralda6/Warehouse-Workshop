package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.application.service.product.ProductService;
import com.gft.warehouse.warehouseworkshop.application.service.stockItem.StockItemService;
import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService productService;

    @Test
    void getProducts() {
        when( productService.getProducts()).thenReturn(
                List.of()
        );

        var result = productController.getProducts();

        assertThat( result )
                .isNotNull()
                .hasSizeGreaterThanOrEqualTo( 0 );
    }

    @Test
    void getProductById() {
        String id = "id_1";
        when( productService.getProductById(id) ).thenReturn(
                Optional.ofNullable(
                        ProductDTO.builder().id(id).build()
                )
        );

        var result = productController.getProductById( id );

        assertThat( result )
                .isNotNull();
        assertThat( result.isPresent() )
                .isTrue();
        assertThat( result.get().getId() )
                .isEqualTo( id );
    }

    @Test
    void saveProduct() {
        ProductDTO product =
                ProductDTO.builder().id("id_1").build();

        when (productService.saveProduct( product ))
                .thenReturn( "Product created with id: " + product.getId());

        var result = productController.saveProduct( product );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat(
                result.substring( result.length()- product.getId().length()))
                .isEqualTo( product.getId() );
    }

    @Test
    void updateProduct() {
        ProductDTO product =
                ProductDTO.builder().id("id_1").build();
        when (productService.updateProduct( product.getId(), product ))
                .thenReturn( "Product updated with id: " + product.getId());

        var result = productController.updateProduct( product.getId(), product );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat(
                result.substring( result.length()- product.getId().length()))
                .isEqualTo( product.getId() );
    }

    @Test
    void deleteStockItem() {
        String id = "id_1";

        when (productService.deleteProduct( id ))
                .thenReturn( "Product deleted with id: " + id);

        var result = productController.deleteProduct(id);

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat(
                result.substring( result.length()- id.length()))
                .isEqualTo( id );
    }
}