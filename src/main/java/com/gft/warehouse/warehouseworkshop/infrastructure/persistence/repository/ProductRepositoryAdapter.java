package com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository;

import com.gft.warehouse.warehouseworkshop.application.service.product.ProductMapperUtils;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.domain.repository.ProductRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    @Autowired
    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll()
                .stream()
                .map(ProductMapperUtils :: toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return productJpaRepository.findById( productId.getId() )
                .map(ProductMapperUtils::toDomain);
    }

    @Override
    public ProductEntity save(Product product) {
        return productJpaRepository.save(
                ProductMapperUtils.toEntity( product )
        );
    }

    @Override
    public void delete(ProductId productId) {
        productJpaRepository.deleteById(
                productId.getId()
        );
    }
}
