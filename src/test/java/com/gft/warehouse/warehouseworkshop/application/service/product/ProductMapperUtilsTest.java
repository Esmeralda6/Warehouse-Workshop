package com.gft.warehouse.warehouseworkshop.application.service.product;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperUtilsTest {

    @Test
    void toDTO_fromDomain() {
        ProductId id = ProductId.builder().id(UUID.randomUUID()).build();
        Product product = Product.builder()
                .productId(id)
                .productName("product_1")
                .build();

        ProductDTO result = ProductMapperUtils.toDTO(product);

        assertThat(result.getId()).isEqualTo(id.getId().toString());
        assertThat(result.getName()).isEqualTo("product_1");
    }

    @Test
    void toDTO_fromEntity() {
        UUID id = UUID.randomUUID();
        ProductEntity entity = ProductEntity.builder()
                .id(id)
                .productName("product_1")
                .build();

        ProductDTO result = ProductMapperUtils.toDTO(entity);

        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getName()).isEqualTo("product_1");
    }

    @Test
    void toDomain_fromDTO() {
        String id = UUID.randomUUID().toString();
        ProductDTO dto = ProductDTO.builder()
                .id(id)
                .name("product_1")
                .build();

        Product result = ProductMapperUtils.toDomain(dto);

        assertThat(result.getProductId().getId().toString()).isEqualTo(id);
        assertThat(result.getProductName()).isEqualTo("product_1");
    }

    @Test
    void toDomain_fromEntity() {
        UUID id = UUID.randomUUID();
        ProductEntity entity = ProductEntity.builder()
                .id(id)
                .productName("product_1")
                .build();

        Product result = ProductMapperUtils.toDomain(entity);

        assertThat(result.getProductId().getId()).isEqualTo(id);
        assertThat(result.getProductName()).isEqualTo("product_1");
    }

    @Test
    void toEntity() {
        ProductId id = ProductId.builder().id(UUID.randomUUID()).build();
        Product product = Product.builder()
                .productId(id)
                .productName("product_1")
                .build();

        ProductEntity result = ProductMapperUtils.toEntity(product);

        assertThat(result.getId()).isEqualTo(id.getId());
        assertThat(result.getProductName()).isEqualTo("product_1");
    }
}
