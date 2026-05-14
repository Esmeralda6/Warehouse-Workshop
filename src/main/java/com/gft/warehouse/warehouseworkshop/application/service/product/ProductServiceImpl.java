package com.gft.warehouse.warehouseworkshop.application.service.product;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    @Override
    public List<ProductDTO> getProducts() {
        return List.of();
    }

    @Override
    public Optional<ProductDTO> getProductById(String id) {
        return Optional.empty();
    }

    @Override
    public String saveProduct(ProductDTO productDTO) {
        return "";
    }

    @Override
    public String updateProduct(String id, ProductDTO productDTO) {
        return "";
    }

    @Override
    public String deleteProduct(String id) {
        return "";
    }
}
