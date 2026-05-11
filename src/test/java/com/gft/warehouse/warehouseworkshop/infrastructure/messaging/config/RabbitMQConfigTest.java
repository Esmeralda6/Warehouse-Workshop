package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMQConfigTest {

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void warehouseExchange_isTopicWithCorrectName() {
        TopicExchange exchange = config.warehouseExchange();

        assertThat(exchange.getName()).isEqualTo(RabbitMQConfig.EXCHANGE);
        assertThat(exchange.getType()).isEqualTo("topic");
    }

    @Test
    void warehouseCreatedQueue_isDurableWithCorrectName() {
        Queue queue = config.warehouseCreatedQueue();

        assertThat(queue.getName()).isEqualTo(RabbitMQConfig.QUEUE_WAREHOUSE_CREATED);
        assertThat(queue.isDurable()).isTrue();
    }

    @Test
    void warehouseCreatedBinding_bindsQueueToExchangeWithRoutingKey() {
        Queue queue = config.warehouseCreatedQueue();
        TopicExchange exchange = config.warehouseExchange();
        Binding binding = config.warehouseCreatedBinding(queue, exchange);

        assertThat(binding.getDestination()).isEqualTo(RabbitMQConfig.QUEUE_WAREHOUSE_CREATED);
        assertThat(binding.getExchange()).isEqualTo(RabbitMQConfig.EXCHANGE);
        assertThat(binding.getRoutingKey()).isEqualTo(RabbitMQConfig.ROUTING_KEY_WAREHOUSE_CREATED);
    }

    @Test
    void messageConverter_isJackson2Json() {
        assertThat(config.messageConverter()).isInstanceOf(Jackson2JsonMessageConverter.class);
    }
}
