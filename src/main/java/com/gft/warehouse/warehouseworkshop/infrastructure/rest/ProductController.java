package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.application.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public List<ProductDTO> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Optional<ProductDTO> getProductById(
            @PathVariable String id
    ){
        return productService.getProductById( id );
    }

    @PostMapping("")
    public String saveProduct(
            @RequestBody ProductDTO productDTO
    ){
        return productService.saveProduct( productDTO );
    }

    @PutMapping("/{id}")
    public String updateProduct(
            @PathVariable String id,
            @RequestBody ProductDTO productDTO
    ){
        return productService.updateProduct( id, productDTO );
    }

    @DeleteMapping("/{id}")
    public String deleteStockItem(
            @PathVariable String id
    ){
        return productService.deleteProduct( id );
    }
}
