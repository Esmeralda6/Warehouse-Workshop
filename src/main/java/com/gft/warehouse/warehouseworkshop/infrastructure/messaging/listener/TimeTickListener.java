package com.gft.warehouse.warehouseworkshop.infrastructure.messaging.listener;

import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.config.RabbitMQConfig;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.event.TimeTickEvent;
import com.gft.warehouse.warehouseworkshop.infrastructure.messaging.time.CurrentDayHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TimeTickListener {

    private static final Logger log = LoggerFactory.getLogger(TimeTickListener.class);

    private final CurrentDayHolder currentDayHolder;

    public TimeTickListener(CurrentDayHolder currentDayHolder) {
        this.currentDayHolder = currentDayHolder;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_TIME_TICK)
    public void onTimeTick(TimeTickEvent event) {
        currentDayHolder.update(event.getTick());
        log.info("Event received [time.advanced.v1]: tick={}", event.getTick());
    }
}
