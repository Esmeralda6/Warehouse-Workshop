package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.publisher;

import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

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
}
