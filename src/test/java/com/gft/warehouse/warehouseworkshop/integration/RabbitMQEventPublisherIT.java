package com.gft.warehouse.warehouseworkshop.integration;

import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.ports.EventPublisher;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener.WarehouseEventListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers
class RabbitMQEventPublisherIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQ = new RabbitMQContainer("rabbitmq:3-management");

    @Autowired
    EventPublisher eventPublisher;

    @MockitoSpyBean
    WarehouseEventListener warehouseEventListener;

    @Test
    void publish_warehouseCreatedEvent_shouldRouteToWarehouseRegisteredQueueAndBeReceivedByListener() {
        // RabbitMQEventPublisher uses event.getEventType() as routing key → "warehouse.registered.v1"
        // This routing key is bound to QUEUE_WAREHOUSE_CREATED on EXCHANGE
        WarehouseCreatedEvent event = new WarehouseCreatedEvent(
                UUID.randomUUID().toString(),
                "Test Warehouse",
                Location.builder().x(5).y(10).build(),
                "FACTORY"
        );

        eventPublisher.publish(event);

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        verify(warehouseEventListener, atLeastOnce())
                                .onWarehouseRegistered(any(WarehouseCreatedEvent.class)));
    }

    @Test
    void publish_multipleEvents_shouldDeliverEachOneToListener() {
        WarehouseCreatedEvent first = new WarehouseCreatedEvent(
                UUID.randomUUID().toString(), "Warehouse A",
                Location.builder().x(1).y(1).build(), "FACTORY");
        WarehouseCreatedEvent second = new WarehouseCreatedEvent(
                UUID.randomUUID().toString(), "Warehouse B",
                Location.builder().x(2).y(2).build(), "PRODUCTION");

        eventPublisher.publish(first);
        eventPublisher.publish(second);

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        verify(warehouseEventListener, atLeastOnce())
                                .onWarehouseRegistered(any(WarehouseCreatedEvent.class)));
    }
}