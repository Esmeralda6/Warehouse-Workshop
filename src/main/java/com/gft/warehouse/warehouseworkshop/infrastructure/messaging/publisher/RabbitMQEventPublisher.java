package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.publisher;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;
import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.ports.EventPublisher;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

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

}
