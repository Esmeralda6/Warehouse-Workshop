package com.gft.warehouse.warehouseworkshop.domain.ports;

import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
