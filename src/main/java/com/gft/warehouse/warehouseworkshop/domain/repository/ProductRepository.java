package com.gft.warehouse.warehouseworkshop.domain.repository;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(ProductId productId);
    ProductEntity save(Product product );
    void delete( ProductId productId);
}
