package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "warehouse.exchange";
    public static final String QUEUE_WAREHOUSE_CREATED = "warehouse.created.queue";
    public static final String ROUTING_KEY_WAREHOUSE_CREATED = "warehouse.created";

    @Bean
    TopicExchange warehouseExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue warehouseCreatedQueue() {
        return new Queue(QUEUE_WAREHOUSE_CREATED, true);
    }

    @Bean
    Binding warehouseCreatedBinding(Queue warehouseCreatedQueue, TopicExchange warehouseExchange) {
        return BindingBuilder.bind(warehouseCreatedQueue).to(warehouseExchange).with(ROUTING_KEY_WAREHOUSE_CREATED);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
