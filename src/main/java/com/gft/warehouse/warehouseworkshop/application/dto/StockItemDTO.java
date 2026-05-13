package com.gft.warehouse.warehouseworkshop.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class StockItemDTO {

    String id;

    @NotBlank
    String productId;
    int quantity;

    @NotBlank
    String warehouseId;

    int minimumQuantity;
}
