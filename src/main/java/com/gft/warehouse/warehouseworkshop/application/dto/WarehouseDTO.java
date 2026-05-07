package com.gft.warehouse.warehouseworkshop.application.dto;

import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@AllArgsConstructor
public class WarehouseDTO {

    //Nullable
    String id;

    @NotBlank
    String name;

    LocationDTO location;

    @Enumerated(EnumType.STRING)
    @NotBlank
    String type;

    //Nullable
    String factoryId;

    /*
    private final WarehouseID warehouseId;
    private final UUID warehouseId;
    private final String warehouseId;

    private final String warehouseName;
    private final String warehouseType;

    private final Location warehouseLocation;
    private final int warehouseLocationX;
    private final int warehouseLocationY;


    private final Map minimumStockRules;
    private final boolean isStockInfinite;

     */
}
