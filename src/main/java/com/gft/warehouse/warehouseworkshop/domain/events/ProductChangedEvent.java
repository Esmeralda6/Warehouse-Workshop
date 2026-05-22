package com.gft.warehouse.warehouseworkshop.domain.events;

import com.gft.warehouse.warehouseworkshop.application.dto.ProductDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Product;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.ProductId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.UUID;

public class ProductChangedEvent extends DomainEvent {

    @Getter
    private final ProductDTOEvent product;
    @Getter
    private final STATUS status;

    public ProductChangedEvent(String productId, String productName) {
        super();
        this.status = STATUS.CREATE;
        this.product = new ProductDTOEvent(productId, productName);
    }



    @Override
    public String getEventType() { return "product.catalogue.update.v1"; }

    enum STATUS{
        CREATE
    }

    @Value
    @AllArgsConstructor
    public class ProductDTOEvent {
        String productId;

        String produtName;
    }
}