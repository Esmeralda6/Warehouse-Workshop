package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.publisher;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.StockVariationType;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.events.StockChangedEvent;
import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.*;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQEventPublisherTest {

    @InjectMocks
    private RabbitMQEventPublisher publisher;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void publish_sendsEventToCorrectExchangeAndRoutingKey() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("wh-1", "Main Warehouse", Location.builder().x(1).y(1).build(), "FACTORY");

        publisher.publish(event);

        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.EXCHANGE, "warehouse.registered.v1", event);
    }

    @Test
    void warehouseRegistered_recordsEventAndPublishesToExchange() throws Exception {
        Warehouse warehouse = Warehouse.builder()
                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                .warehouseName("Test Warehouse")
                .warehouseLocation(Location.builder().x(1).y(2).build())
                .warehouseType(WarehouseType.FACTORY)
                .factoryId(FactoryId.builder().id(null).build())
                .build();

        publisher.warehouseRegistered(warehouse);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq("warehouse.registered.v1"),
                any(WarehouseCreatedEvent.class));
    }

    @Test
    void stockChanged_recordsEventAndPublishesToExchange() throws Exception {
        StockItem stockItem = StockItem.builder()
                .stockItemId(StockItemId.builder().id(UUID.randomUUID()).build())
                .productId(ProductId.builder().id(UUID.randomUUID()).build())
                .warehouseId(WarehouseId.builder().id(UUID.randomUUID()).build())
                .quantity(Quantity.builder().value(10).build())
                .minimumQuantityRule(Quantity.builder().value(2).build())
                .build();

        publisher.stockChanged(stockItem, StockVariationType.INCREASE);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq("warehouse.stock.changed.v1"),
                any(StockChangedEvent.class));
    }
}
