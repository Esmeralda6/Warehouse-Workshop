package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.DeliveryCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryCompletedListener {

    private static final Logger log = LoggerFactory.getLogger(DeliveryCompletedListener.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DELIVERY_COMPLETED)
    public void onDeliveryCompleted(DeliveryCompletedEvent event) {
        log.info("Event received [delivery.completed.v1]: shipmentId={}, truckId={}",
                event.getShipmentId(), event.getTruckId());
    }
}
