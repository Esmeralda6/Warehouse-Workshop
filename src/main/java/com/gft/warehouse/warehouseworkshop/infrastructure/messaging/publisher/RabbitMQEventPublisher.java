package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.publisher;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.application.dto.ItemRequestDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.ShipmentRequestDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.StockVariationType;
import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;
import com.gft.warehouse.warehouseworkshop.domain.events.ProductChangedEvent;
import com.gft.warehouse.warehouseworkshop.domain.events.ShipmentRequestedEvent;
import com.gft.warehouse.warehouseworkshop.domain.events.StockChangedEvent;
import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.ports.EventPublisher;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.time.CurrentDayHolder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RabbitMQEventPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

    }

    @Override
    public void publish(DomainEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, event.getEventType(), event);
    }

    @Override
    public void warehouseRegistered( Warehouse warehouse) throws Exception{
        warehouse.recordEvent(new WarehouseCreatedEvent(
                warehouse.getWarehouseId().getId().toString(),
                warehouse.getWarehouseName(),
                warehouse.getWarehouseLocation(),
                warehouse.getWarehouseType().name()
        ));
        warehouse.getDomainEvents().forEach(this::publish);
        warehouse.clearDomainEvents();
    }

    @Override
    public void stockChanged(StockItem stockItem, StockVariationType stockVariationType) throws Exception {
        stockItem.recordEvent( new StockChangedEvent(
                stockItem.getProductId().getId().toString(),
                stockItem.getQuantity().getValue(),
                stockVariationType.toString()
        ));
        stockItem.getDomainEvents().forEach(this::publish);
        stockItem.clearDomainEvents();
    }

    @Override
    public void productChanged(Product product) throws Exception {
        product.recordEvent(new ProductChangedEvent(
                product.getProductId().getId().toString(),
                product.getProductName()
        ));
        product.getDomainEvents().forEach(this::publish);
        product.clearDomainEvents();
    }

    @Override
    public void shipmentRequested(Warehouse warehosueDestination, ShipmentRequestDTO shipmentRequestDTO) throws Exception{
        int currentDay = CurrentDayHolder.getCurrentDay();

        // This should be its own persistant entity, not saved for now
        String shipmentId = shipmentRequestDTO.getShipmentId() != null
                ? shipmentRequestDTO.getShipmentId().toString()
                : UUID.randomUUID().toString();

        warehosueDestination.recordEvent(
                new ShipmentRequestedEvent(
                        shipmentId,
                        shipmentRequestDTO.getOriginId(),
                        shipmentRequestDTO.getDestinationId(),
                        shipmentRequestDTO.getItems().stream().map(this::itemMapper).toList(),
                        currentDay
                )
        );
    }

    private ShipmentRequestedEvent.Item itemMapper(ItemRequestDTO itemRequestDTO){
        return new ShipmentRequestedEvent.Item(
                itemRequestDTO.getMaterialType(),
                itemRequestDTO.getQuantity()
        );
    }
}
