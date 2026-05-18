package com.gft.warehouse.warehouseworkshop.e2e;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.DeliveryCompletedEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.FactoryRegisteredEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.MaterialsRequestedEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.ProductionOrderCompletedEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.TimeTickEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.time.CurrentDayHolder;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Por qué no usamos @MockitoSpyBean aquí:
 * Spring AMQP's listener container almacena la referencia al bean en el momento
 * de arrancar el contexto, antes de que @MockitoSpyBean envuelva el bean en un proxy
 * de Mockito. Resultado: el container invoca el bean original y el spy nunca lo ve.
 *
 * Estrategia alternativa: verificar que el mensaje fue consumido del queue
 * (cola vacía = listener lo procesó) usando RabbitAdmin. Esto prueba toda la cadena
 * real: exchange → routing key → binding → queue → listener container → listener.
 */
@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers
class RabbitMQListenerE2E {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQ = new RabbitMQContainer("rabbitmq:3-management");

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    CurrentDayHolder currentDayHolder;

    // ─── FactoryRegisteredListener ───────────────────────────────────────────────

    @Test
    void factoryRegisteredListener_E2E_whenEventPublishedToExchange_shouldConsumeMessageFromQueue() {
        FactoryRegisteredEvent event = new FactoryRegisteredEvent();
        event.setFactoryId(UUID.randomUUID().toString());
        event.setWarehouseId(UUID.randomUUID().toString());

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_FACTORY_REGISTERED, event);

        await().atMost(Duration.ofSeconds(5))
                .until(() -> queueIsEmpty(QUEUE_FACTORY_REGISTERED));

        assertThat(queueHasActiveConsumer(QUEUE_FACTORY_REGISTERED)).isTrue();
    }

    // ─── DeliveryCompletedListener ───────────────────────────────────────────────

    @Test
    void deliveryCompletedListener_E2E_whenEventPublishedToExchange_shouldConsumeMessageFromQueue() {
        DeliveryCompletedEvent event = new DeliveryCompletedEvent();
        event.setShipmentId(UUID.randomUUID().toString());
        event.setTruckId("TRUCK-42");
        DeliveryCompletedEvent.Item item = new DeliveryCompletedEvent.Item();
        item.setMaterialType("STEEL");
        item.setQuantity(100);
        event.setItems(List.of(item));

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_DELIVERY_COMPLETED, event);

        await().atMost(Duration.ofSeconds(5))
                .until(() -> queueIsEmpty(QUEUE_DELIVERY_COMPLETED));

        assertThat(queueHasActiveConsumer(QUEUE_DELIVERY_COMPLETED)).isTrue();
    }

    // ─── MaterialsRequestedListener ──────────────────────────────────────────────

    @Test
    void materialsRequestedListener_E2E_whenEventPublishedToExchange_shouldConsumeMessageFromQueue() {
        MaterialsRequestedEvent event = new MaterialsRequestedEvent();
        MaterialsRequestedEvent.Item item = new MaterialsRequestedEvent.Item();
        item.setProductId(UUID.randomUUID().toString());
        item.setQuantity(50);
        event.setItems(List.of(item));

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_MATERIALS_REQUESTED, event);

        await().atMost(Duration.ofSeconds(5))
                .until(() -> queueIsEmpty(QUEUE_MATERIALS_REQUESTED));

        assertThat(queueHasActiveConsumer(QUEUE_MATERIALS_REQUESTED)).isTrue();
    }

    // ─── ProductionOrderCompletedListener ────────────────────────────────────────

    @Test
    void productionOrderCompletedListener_E2E_whenEventPublishedToExchange_shouldConsumeMessageFromQueue() {
        ProductionOrderCompletedEvent event = new ProductionOrderCompletedEvent();
        event.setProductionOrderId(UUID.randomUUID().toString());
        event.setProductId(UUID.randomUUID().toString());
        event.setWarehouseOrderId(UUID.randomUUID().toString());
        event.setFactoryAsign(UUID.randomUUID().toString());
        event.setQuantity(10);
        event.setStatus("COMPLETED");

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_PRODUCTION_ORDER_COMPLETED, event);

        await().atMost(Duration.ofSeconds(5))
                .until(() -> queueIsEmpty(QUEUE_PRODUCTION_ORDER_COMPLETED));

        assertThat(queueHasActiveConsumer(QUEUE_PRODUCTION_ORDER_COMPLETED)).isTrue();
    }

    // ─── TimeTickListener ────────────────────────────────────────────────────────

    @Test
    void timeTickListener_E2E_whenEventPublishedDirectlyToQueue_shouldUpdateCurrentDayHolder() {
        // TIME_EXCHANGE tiene shouldDeclare=false (pertenece al servicio ms-time),
        // así que enviamos directo al queue via el exchange por defecto.
        TimeTickEvent event = new TimeTickEvent();
        event.setTick(42);

        rabbitTemplate.convertAndSend(QUEUE_TIME_TICK, event);

        // TimeTickListener actualiza CurrentDayHolder → estado observable sin spy
        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        assertThat(currentDayHolder.getCurrentDay()).isEqualTo(42));
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    private boolean queueIsEmpty(String queueName) {
        QueueInformation info = rabbitAdmin.getQueueInfo(queueName);
        return info != null && info.getMessageCount() == 0;
    }

    private boolean queueHasActiveConsumer(String queueName) {
        QueueInformation info = rabbitAdmin.getQueueInfo(queueName);
        return info != null && info.getConsumerCount() > 0;
    }
}
