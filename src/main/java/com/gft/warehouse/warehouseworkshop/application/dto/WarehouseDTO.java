package com.gft.warehouse.warehouseworkshop.application.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;


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
}
