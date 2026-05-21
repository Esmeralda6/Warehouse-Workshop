package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.events.DomainEvent;
import com.gft.warehouse.warehouseworkshop.domain.exceptions.InsuficientStockException;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Quantity;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.StockItemId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class StockItem {
    private final StockItemId stockItemId;
    private final ProductId productId;
    private Quantity quantity;
    private final WarehouseId warehouseId;
    private Quantity minimumQuantityRule;

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

    public boolean isEnough(Quantity needed) {
        return this.quantity.getValue() >= needed.getValue();
    }

    public void add(Quantity qty) {
        this.quantity = Quantity.builder().value(this.quantity.getValue() + qty.getValue()).build();
    }

    public void subtract(Quantity qty) {
        if (qty.getValue() > this.quantity.getValue()) {
            throw new InsuficientStockException("Not enough stock to subtract");
        }
        this.quantity = Quantity.builder().value(this.quantity.getValue() - qty.getValue()).build();
    }

    public boolean hasProduct(ProductId productId) {
        return this.productId.getId().equals(productId.getId());
    }

    
}
