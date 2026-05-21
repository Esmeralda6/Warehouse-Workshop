package com.gft.warehouse.warehouseworkshop.domain.ports;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.application.dto.ShipmentRequestDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.StockVariationType;
import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);

    void warehouseRegistered(Warehouse warehouse) throws Exception;
    void stockChanged(StockItem stockItem, StockVariationType stockVariationType) throws Exception;
    void productChanged(Product product) throws Exception;
    void shipmentRequested(Warehouse warehouseDestination, ShipmentRequestDTO shipmentRequestDTO) throws Exception;
}
