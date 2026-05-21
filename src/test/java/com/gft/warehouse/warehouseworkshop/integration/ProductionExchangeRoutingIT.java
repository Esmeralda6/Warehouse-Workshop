package com.gft.warehouse.warehouseworkshop.integration;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.DeliveryCompletedEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.FactoryRegisteredEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.MaterialsRequestedEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.ProductionOrderCompletedEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener.DeliveryCompletedListener;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener.FactoryRegisteredListener;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener.MaterialsRequestedListener;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener.ProductionOrderCompletedListener;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

/**
 * Verifies that each queue bound to production.exchange receives messages
 * published with the correct routing key.
 *
 * These tests protect against configuration regressions: a wrong routing key
 * or a broken exchange binding would silently drop messages in production.
 * Only a real RabbitMQ container can catch that class of error.
 */
@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers
class ProductionExchangeRoutingIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQ = new RabbitMQContainer("rabbitmq:3-management");

    @Autowired
    RabbitTemplate rabbitTemplate;

    @MockitoSpyBean
    FactoryRegisteredListener factoryRegisteredListener;

    @MockitoSpyBean
    ProductionOrderCompletedListener productionOrderCompletedListener;

    @MockitoSpyBean
    DeliveryCompletedListener deliveryCompletedListener;

    @MockitoSpyBean
    MaterialsRequestedListener materialsRequestedListener;

    // ─── factory.registered.v1 ───────────────────────────────────────────────────

    @Test
    void factoryRegistered_publishedToProductionExchange_shouldReachFactoryRegisteredListener() {
        FactoryRegisteredEvent event = new FactoryRegisteredEvent();
        event.setFactoryId(UUID.randomUUID().toString());
        event.setWarehouseId(UUID.randomUUID().toString());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_FACTORY_REGISTERED,
                event);

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        verify(factoryRegisteredListener, atLeastOnce())
                                .onFactoryRegistered(any(FactoryRegisteredEvent.class)));
    }

    // ─── production.order.completed.v1 ───────────────────────────────────────────

    @Test
    void productionOrderCompleted_publishedToProductionExchange_shouldReachProductionOrderCompletedListener() {
        ProductionOrderCompletedEvent event = new ProductionOrderCompletedEvent();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_PRODUCTION_ORDER_COMPLETED,
                event);

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        verify(productionOrderCompletedListener, atLeastOnce())
                                .onProductionOrderCompleted(any(ProductionOrderCompletedEvent.class)));
    }

    // ─── delivery.completed.v1 ────────────────────────────────────────────────────

    @Test
    void deliveryCompleted_publishedToProductionExchange_shouldReachDeliveryCompletedListener() {
        DeliveryCompletedEvent event = new DeliveryCompletedEvent();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_DELIVERY_COMPLETED,
                event);

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        verify(deliveryCompletedListener, atLeastOnce())
                                .onDeliveryCompleted(any(DeliveryCompletedEvent.class)));
    }

    // ─── product.materials.requested.v1 ──────────────────────────────────────────

    @Test
    void materialsRequested_publishedToProductionExchange_shouldReachMaterialsRequestedListener() {
        MaterialsRequestedEvent event = new MaterialsRequestedEvent();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_MATERIALS_REQUESTED,
                event);

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        verify(materialsRequestedListener, atLeastOnce())
                                .onMaterialsRequested(any(MaterialsRequestedEvent.class)));
    }
}
