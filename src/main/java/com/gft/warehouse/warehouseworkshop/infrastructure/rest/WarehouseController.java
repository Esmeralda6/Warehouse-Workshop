package com.gft.warehouse.warehouseworkshop.infrastructure.rest;

import com.gft.warehouse.warehouseworkshop.application.dto.FactoryIdDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.application.service.warehouse.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/warehouses")
@Slf4j
@Tag(name = "Warehouse", description = "Warehouse management operations")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("")
    @Operation(
            summary = "Get all warehouses",
            description = "Retrieves the list of all currently existing warehouses" )
    @ApiResponse(
            responseCode = "200",
            description = "List retrieved correctly",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseDTO.class)
            )
    )
    public List<WarehouseDTO> getWarehouses(){
        return warehouseService.getWarehouses();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get warehouse by Id",
            description = "Retrieves a single warehouse given its unique id" )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse retrieved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseDTO.class)
            )
    )
    public Optional<WarehouseDTO> getWarehouseById(
            @Parameter(description = "Warehouse Id as UUID", example = "47d4b6fa-5a90-4afc-8e4c-a53f4541182f")
            @PathVariable String id
    ){
        return warehouseService.getWarehouseById(id) ;
    }

    @PostMapping("")
    @Operation(
            summary = "Save Warehouse",
            description = "Creates a new warehouse, accepting a specific UUID or assigning a randomly generated one." )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse saved",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject("Warehouse saved with id: 1")
            )
    )
    public String saveWarehouse(
            @io.swagger.v3.oas.annotations.parameters.RequestBody
                    (description = "Accepts nullable id",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WarehouseDTO.class) )
                    )
            @RequestBody WarehouseDTO warehouse
    ){
        return warehouseService.saveWarehouse( warehouse );
    }

    @Operation(
            summary = "Update Warehouse",
            description = "Updates a given warehouse, retrieved through its unique id." )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse updated",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject("Warehouse updated with id: 1")
            )
    )
    @PutMapping("/{id}")
    public String updateWarehouse(
            @Parameter(description = "Warehouse Id as UUID", example = "47d4b6fa-5a90-4afc-8e4c-a53f4541182f")
            @PathVariable String id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody
                    (description = "Ignores RequestBody id, immutable.",
                            required = true,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WarehouseDTO.class) )
                    )
            @RequestBody WarehouseDTO warehouse
    ){
        return warehouseService.updateWarehouse( id, warehouse );
    }

    @Operation(
            summary = "Delete Warehouse",
            description = "Deletes a given warehouse, retrieved through its unique id." )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse deleted",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject("Warehouse updated with id: 1")
            )
    )
    @DeleteMapping("/{id}")
    public String deleteWarehouse(
            @Parameter(description = "Warehouse Id as UUID", example = "47d4b6fa-5a90-4afc-8e4c-a53f4541182f")
            @PathVariable String id
    ){
        return warehouseService.deleteWarehouse( id );
    }

    //Assign warehouse to a new factory

    //Find Warehouse without assigned FactoryId
        //Currently only checks if FactoryId==null && Type==FACTORY, and returns first found
    @GetMapping("/available")
    @Operation(
            summary = "Find available warehouse",
            description = "Find Warehouse without assigned FactoryId. \nCurrently only checks if FactoryId==null && Type==FACTORY, and returns first found" )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse retrieved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseDTO.class)
            )
    )
    public Optional<WarehouseDTO> findAvailableWarehouse(){
        return warehouseService.findAvailableWarehouse();
    }

    //Assign Factory to an existing warehouse,
    @PatchMapping("/assignFactory/{warehouseId}")
    @Operation(
            summary = "Assign Factory to a Warehouse",
            description = "Patches the factoryId attribute of a warehouse retrived by its unique id with the provided factoryId." )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse updated",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject("Warehouse with id: 1 succesfully assigned to factory with id 2")
            )
    )
    public String assignFactory(
            @Parameter(description = "Warehouse Id as UUID", example = "47d4b6fa-5a90-4afc-8e4c-a53f4541182f")
            @PathVariable String warehouseId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody
                    (description = "factoryId required.",
                            required = true,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FactoryIdDTO.class) )
                    )
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
