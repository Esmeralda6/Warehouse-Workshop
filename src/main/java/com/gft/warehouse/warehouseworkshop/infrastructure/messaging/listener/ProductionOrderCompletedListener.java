package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.ProductionOrderCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductionOrderCompletedListener {

    private static final Logger log = LoggerFactory.getLogger(ProductionOrderCompletedListener.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PRODUCTION_ORDER_COMPLETED)
    public void onProductionOrderCompleted(ProductionOrderCompletedEvent event) {
        log.info("Event received [production.order.completed.v1]: productionOrderId={}, productId={}, status={}",
                event.getProductionOrderId(), event.getProductId(), event.getStatus());
    }
}
