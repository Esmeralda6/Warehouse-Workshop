package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class Product {
    private final ProductId productId;
    private String productName;

    @Builder.Default
    private List<DomainEvent> domainEvents = new ArrayList<>();

    public void recordEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
