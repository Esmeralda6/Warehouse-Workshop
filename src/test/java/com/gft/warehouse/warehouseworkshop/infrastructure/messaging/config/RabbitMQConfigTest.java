package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RabbitMQConfigTest {

    @Mock
    private ConnectionFactory connectionFactory;

    private RabbitMQConfig config;

    @BeforeEach
    void setUp() {
        config = new RabbitMQConfig();
    }

    @Test
    void warehouseExchange_returnsTopicExchangeWithCorrectName() {
        TopicExchange exchange = config.warehouseExchange();

        assertThat(exchange).isNotNull();
        assertThat(exchange.getName()).isEqualTo(RabbitMQConfig.EXCHANGE);
    }

    @Test
    void timeExchange_returnsTopicExchangeWithCorrectName() {
        TopicExchange exchange = config.timeExchange();

        assertThat(exchange).isNotNull();
        assertThat(exchange.getName()).isEqualTo(RabbitMQConfig.TIME_EXCHANGE);
    }

    @Test
    void productionExchange_returnsTopicExchangeWithCorrectName() {
        TopicExchange exchange = config.productionExchange();

        assertThat(exchange).isNotNull();
        assertThat(exchange.getName()).isEqualTo(RabbitMQConfig.PRODUCTION_EXCHANGE);
    }

    @Test
    void queues_returnDurableQueuesWithCorrectNames() {
        Queue warehouseCreated = config.warehouseCreatedQueue();
        Queue timeTick = config.timeTickQueue();
        Queue deliveryCompleted = config.deliveryCompletedQueue();
        Queue materialsRequested = config.materialsRequestedQueue();
        Queue productionOrderCompleted = config.productionOrderCompletedQueue();
        Queue factoryRegistered = config.factoryRegisteredQueue();

        assertThat(warehouseCreated.getName()).isEqualTo(RabbitMQConfig.QUEUE_WAREHOUSE_CREATED);
        assertThat(timeTick.getName()).isEqualTo(RabbitMQConfig.QUEUE_TIME_TICK);
        assertThat(deliveryCompleted.getName()).isEqualTo(RabbitMQConfig.QUEUE_DELIVERY_COMPLETED);
        assertThat(materialsRequested.getName()).isEqualTo(RabbitMQConfig.QUEUE_MATERIALS_REQUESTED);
        assertThat(productionOrderCompleted.getName()).isEqualTo(RabbitMQConfig.QUEUE_PRODUCTION_ORDER_COMPLETED);
        assertThat(factoryRegistered.getName()).isEqualTo(RabbitMQConfig.QUEUE_FACTORY_REGISTERED);
    }

    @Test
    void bindings_returnBindingsWithCorrectRoutingKeys() {
        Binding warehouseCreated = config.warehouseCreatedBinding(config.warehouseCreatedQueue(), config.warehouseExchange());
        Binding timeTick = config.timeTickBinding(config.timeTickQueue(), config.timeExchange());
        Binding deliveryCompleted = config.deliveryCompletedBinding(config.deliveryCompletedQueue(), config.productionExchange());
        Binding materialsRequested = config.materialsRequestedBinding(config.materialsRequestedQueue(), config.productionExchange());
        Binding productionOrderCompleted = config.productionOrderCompletedBinding(config.productionOrderCompletedQueue(), config.productionExchange());
        Binding factoryRegistered = config.factoryRegisteredBinding(config.factoryRegisteredQueue(), config.productionExchange());

        assertThat(warehouseCreated.getRoutingKey()).isEqualTo(RabbitMQConfig.ROUTING_KEY_WAREHOUSE_CREATED);
        assertThat(timeTick.getRoutingKey()).isEqualTo(RabbitMQConfig.ROUTING_KEY_TIME_TICK);
        assertThat(deliveryCompleted.getRoutingKey()).isEqualTo(RabbitMQConfig.ROUTING_KEY_DELIVERY_COMPLETED);
        assertThat(materialsRequested.getRoutingKey()).isEqualTo(RabbitMQConfig.ROUTING_KEY_MATERIALS_REQUESTED);
        assertThat(productionOrderCompleted.getRoutingKey()).isEqualTo(RabbitMQConfig.ROUTING_KEY_PRODUCTION_ORDER_COMPLETED);
        assertThat(factoryRegistered.getRoutingKey()).isEqualTo(RabbitMQConfig.ROUTING_KEY_FACTORY_REGISTERED);
    }

    @Test
    void messageConverter_returnsJackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = config.messageConverter();

        assertThat(converter).isNotNull();
    }

    @Test
    void rabbitTemplate_returnsConfiguredTemplate() {
        RabbitTemplate template = config.rabbitTemplate(connectionFactory);

        assertThat(template).isNotNull();
    }
}