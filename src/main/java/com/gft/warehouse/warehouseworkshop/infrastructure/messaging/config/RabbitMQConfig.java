package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE                                  = "warehouses.exchange";
    public static final String TIME_EXCHANGE                             = "ms-time.exchange";

    public static final String QUEUE_WAREHOUSE_CREATED                   = "warehouse" + ".registered.queue";
    public static final String ROUTING_KEY_WAREHOUSE_CREATED             = "warehouse.registered.v1";

    public static final String QUEUE_TIME_TICK                           = "warehouse.time.tick.queue";
    public static final String ROUTING_KEY_TIME_TICK                     = "time.advanced.v1";

    public static final String QUEUE_DELIVERY_COMPLETED                  = "warehouse.delivery.completed.queue";
    public static final String ROUTING_KEY_DELIVERY_COMPLETED            = "delivery.completed.v1";

    public static final String QUEUE_MATERIALS_REQUESTED                 = "warehouse.materials.requested.queue";
    public static final String ROUTING_KEY_MATERIALS_REQUESTED           = "product.materials.requested.v1";

    public static final String QUEUE_PRODUCTION_ORDER_COMPLETED          = "warehouse.production.order.completed.queue";
    public static final String ROUTING_KEY_PRODUCTION_ORDER_COMPLETED    = "production.order.completed.v1";

    public static final String QUEUE_FACTORY_REGISTERED                  = "warehouse.factory.registered.queue";
    public static final String ROUTING_KEY_FACTORY_REGISTERED            = "factory.registered.v1";

    public static final String ROUTING_KEY_PRODUCT_ORDER_BLOCKED         = "product.order.blocked.v1";

    @Bean
    TopicExchange warehouseExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    TopicExchange timeExchange() {
        TopicExchange exchange = new TopicExchange(TIME_EXCHANGE);
        exchange.setShouldDeclare(false);
        return exchange;
    }

    @Bean
    Queue warehouseCreatedQueue() {
        return new Queue(QUEUE_WAREHOUSE_CREATED, true);
    }

    @Bean
    Queue timeTickQueue() {
        return new Queue(QUEUE_TIME_TICK, true);
    }

    @Bean
    Binding warehouseCreatedBinding(Queue warehouseCreatedQueue, TopicExchange warehouseExchange) {
        return BindingBuilder.bind(warehouseCreatedQueue)
                .to(warehouseExchange)
                .with(ROUTING_KEY_WAREHOUSE_CREATED);
    }

    @Bean
    Binding timeTickBinding(Queue timeTickQueue, TopicExchange timeExchange) {
        return BindingBuilder.bind(timeTickQueue)
                .to(timeExchange)
                .with(ROUTING_KEY_TIME_TICK);
    }

    @Bean
    Queue deliveryCompletedQueue() {
        return new Queue(QUEUE_DELIVERY_COMPLETED, true);
    }

    @Bean
    Binding deliveryCompletedBinding(Queue deliveryCompletedQueue, TopicExchange warehouseExchange) {
        return BindingBuilder.bind(deliveryCompletedQueue)
                .to(warehouseExchange)
                .with(ROUTING_KEY_DELIVERY_COMPLETED);
    }

    @Bean
    Queue materialsRequestedQueue() {
        return new Queue(QUEUE_MATERIALS_REQUESTED, true);
    }

    @Bean
    Binding materialsRequestedBinding(Queue materialsRequestedQueue, TopicExchange warehouseExchange) {
        return BindingBuilder.bind(materialsRequestedQueue)
                .to(warehouseExchange)
                .with(ROUTING_KEY_MATERIALS_REQUESTED);
    }

    @Bean
    Queue productionOrderCompletedQueue() {
        return new Queue(QUEUE_PRODUCTION_ORDER_COMPLETED, true);
    }

    @Bean
    Binding productionOrderCompletedBinding(Queue productionOrderCompletedQueue, TopicExchange warehouseExchange) {
        return BindingBuilder.bind(productionOrderCompletedQueue)
                .to(warehouseExchange)
                .with(ROUTING_KEY_PRODUCTION_ORDER_COMPLETED);
    }

    @Bean
    Queue factoryRegisteredQueue() {
        return new Queue(QUEUE_FACTORY_REGISTERED, true);
    }

    @Bean
    Binding factoryRegisteredBinding(Queue factoryRegisteredQueue, TopicExchange warehouseExchange) {
        return BindingBuilder.bind(factoryRegisteredQueue)
                .to(warehouseExchange)
                .with(ROUTING_KEY_FACTORY_REGISTERED);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        Jackson2JsonMessageConverter publishConverter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        publishConverter.setJavaTypeMapper(typeMapper);
        template.setMessageConverter(publishConverter);
        return template;
    }
}
