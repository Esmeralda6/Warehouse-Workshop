package com.gft.warehouse.warehouseworkshop.application.service.product;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class ProductMapperUtils {

    @lombok.Generated
    protected ProductMapperUtils() {}

    public static ProductDTO toDTO(Product product){

        return ProductDTO.builder()
                .id( product.getProductId().getId().toString() )
                .name(product.getProductName() )
                .build();
    }

    public static ProductDTO toDTO(ProductEntity productEntity){

        return ProductDTO.builder()
                .id( productEntity.getId().toString() )
                .name( productEntity.getProductName() )
                .build();
    }

    public static Product toDomain(ProductDTO productDTO){

        return Product.builder()
                .productId(
                        ProductId.builder()
                                .id( UUID.fromString( productDTO.getId() ) )
                                .build()
                )
                .productName( productDTO.getName() )
                .build();
    }

    public static Product toDomain(ProductEntity productEntity){
        return Product.builder()
                .productId(
                        ProductId.builder()
                                .id( productEntity.getId() )
                                .build()
                )
                .productName( productEntity.getProductName() )
                .build();
    }

    public static ProductEntity toEntity(Product product){
        return ProductEntity.builder()
                .id( product.getProductId().getId() )
                .productName( product.getProductName() )
                .build();
    }
}
