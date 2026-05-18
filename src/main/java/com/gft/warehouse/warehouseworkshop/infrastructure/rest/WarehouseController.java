package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.FactoryIdDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.application.service.warehouse.WarehouseService;
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

    @GetMapping("")
    public List<WarehouseDTO> getWarehouses(){
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

    @PutMapping("/{id}")
    public String updateWarehouse(
            @PathVariable String id,
            @RequestBody WarehouseDTO warehouse
    ){
        return warehouseService.updateWarehouse( id, warehouse );
    }

    @DeleteMapping("/{id}")
    public String deleteWarehouse(
            @PathVariable String id
    ){
        return warehouseService.deleteWarehouse( id );
    }

    //Assign warehouse to a new factory

    //Find Warehouse without assigned FactoryId
        //Currently only checks if FactoryId==null && Type==FACTORY, and returns first found
    @GetMapping("/available")
    public Optional<WarehouseDTO> findAvailableWarehouse(){
        return warehouseService.findAvailableWarehouse();
    }

    //Assign Factory to an existing warehouse,
    @PatchMapping("/assignFactory/{warehouseId}")
    public String assignFactory(
            @PathVariable String warehouseId,
            @RequestBody FactoryIdDTO factoryId
            ){
        return warehouseService.assignFactoryId(warehouseId, factoryId);
    }

    /*
    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException ex, HttpServletRequest request) {
        return ClassUtils.getShortName(ex.getClass());
    }
    */

}
