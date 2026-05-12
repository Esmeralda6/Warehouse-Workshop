package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WarehouseEventListener {

    private static final Logger log = LoggerFactory.getLogger(WarehouseEventListener.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_WAREHOUSE_CREATED)
    public void onWarehouseCreated(WarehouseCreatedEvent event) {
        log.info("Event received [{}]: warehouse '{}' of type '{}' created at {}",
                event.getEventId(), event.getWarehouseName(), event.getWarehouseType(), event.getOccurredOn());
    }
}
