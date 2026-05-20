package com.gft.warehouse.warehouseworkshop.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ProductDTO {
    String id;

    String name;
}
