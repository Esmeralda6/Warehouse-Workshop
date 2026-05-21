package com.gft.warehouse.warehouseworkshop.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class ShipmentRequestDTO {
    UUID shipmentId;
    String originId;
    String destinationId;
    List<ItemRequestDTO> items;
    int requestedAt;
}
/*
{
  "shipmentId": "uuid",
  "originId": "warehouse-north-01",
  "destinationId": "warehouse-south-03",
  "items": [{ "materialType": "wood", "quantity": 6 }],
  "requestedAt": 3
}

* */