package com.gft.warehouse.warehouseworkshop.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class DeliveryCompletedDTO {
    UUID shipmentId;
    UUID truckId;
    List<ItemRequestDTO> items;
    LocationDTO location;
    int completedAt;
}
