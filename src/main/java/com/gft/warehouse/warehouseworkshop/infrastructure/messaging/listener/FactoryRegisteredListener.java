package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.FactoryRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FactoryRegisteredListener {

    private static final Logger log = LoggerFactory.getLogger(FactoryRegisteredListener.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_FACTORY_REGISTERED)
    public void onFactoryRegistered(FactoryRegisteredEvent event) {
        log.info("Event received [factory.registered.v1]: factoryId={}, warehouseId={}",
                event.getFactoryId(), event.getWarehouseId());
    }
}
