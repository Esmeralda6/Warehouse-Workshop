package com.gft.warehouse.warehouseworkshop.application.service.product;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getProducts();
    Optional<ProductDTO> getProductById(String id);
    String saveProduct( ProductDTO productDTO );
    String updateProduct( String id, ProductDTO productDTO);
    String deleteProduct( String id );
}
