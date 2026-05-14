package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.ExternalProductDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.application.service.WarehouseService;
import com.gft.warehouse.warehouseworkshop.infrastructure.productProvider.ProductLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/warehouses")
@Slf4j
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ProductLoaderService productLoaderService;

    @GetMapping("/list")
    public List<WarehouseDTO> getWarehouses(){
        log.info("GET LIST");
        return warehouseService.getWarehouses();
    }

    @GetMapping("/{id}")

    public Optional<WarehouseDTO> getWarehouseById(
            @PathVariable String id
    ){
        return warehouseService.getWarehouseById(id) ;
    }

    @PostMapping("")
    public String saveWarehouse(
            @RequestBody WarehouseDTO warehouse
    ){
        return warehouseService.saveWarehouse( warehouse );
    }

    @GetMapping("/products")
    public List<ExternalProductDTO> getRemoteProducts() {
        log.info("GET REMOTE PRODUCTS FROM GITHUB");
        return productLoaderService.fetchProducts();
    }
}
