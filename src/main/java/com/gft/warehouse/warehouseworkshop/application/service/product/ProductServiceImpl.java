package com.gft.warehouse.warehouseworkshop.application.service.product;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.application.service.stockItem.StockItemMapperUtils;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.domain.repository.ProductRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.StockItemId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapperUtils::toDTO)
                .toList();
    }

    @Override
    public Optional<ProductDTO> getProductById(String id) {
        return productRepository.findById(ProductId.builder().id( UUID.fromString( id ) ).build() )
                .map(ProductMapperUtils::toDTO);
    }

    @Override
    public String saveProduct(ProductDTO productDTO) {

        if ( productDTO.getId() == null || productDTO.getId().isBlank() )
            productDTO = ProductDTO.builder()
                    .id( UUID.randomUUID().toString() )
                    .name( productDTO.getName() )
                    .build();

        Product product = ProductMapperUtils.toDomain(productDTO);

        ProductEntity savedProduct  = productRepository.save(
                product
        );
        log.info( savedProduct.toString() );
        return "Product saved with id: " + savedProduct.getId().toString();
    }

    @Override
    public String updateProduct(String id, ProductDTO productDTO) {
        Optional<ProductDTO> product = productRepository.findById(
                        ProductId.builder().id(UUID.fromString(id)).build())
                .map(updatedProduct -> {
                    updatedProduct.setProductName(
                            productDTO.getName()
                    );
                    return ProductMapperUtils.toDTO( productRepository.save(updatedProduct));
                });
        if (product.isPresent()){
            return "Product with id " + id + " succesfully updated";
        }
        return "Product with id " + id + " not found.";
    }

    @Override
    public String deleteProduct(String id) {
        ProductId productId  = ProductId.builder().id(UUID.fromString(id)).build();
        if(
                productRepository.findById( productId ).isPresent()
        ){
            productRepository.delete( productId );
            return "Product with id " + id + " succesfully deleted.";
        }
        return "Product with id " + id + " was not found.";
    }
}
