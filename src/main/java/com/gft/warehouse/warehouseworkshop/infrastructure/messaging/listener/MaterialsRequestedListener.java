package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.MaterialsRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MaterialsRequestedListener {

    private static final Logger log = LoggerFactory.getLogger(MaterialsRequestedListener.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_MATERIALS_REQUESTED)
    public void onMaterialsRequested(MaterialsRequestedEvent event) {
        log.info("Event received [product.materials.requested.v1]: {} item(s) requested",
                event.getItems() != null ? event.getItems().size() : 0);
    }
}
