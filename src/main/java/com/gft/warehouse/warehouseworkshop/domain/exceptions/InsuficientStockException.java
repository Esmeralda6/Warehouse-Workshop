package com.gft.warehouse.warehouseworkshop.domain.exceptions;

public class InsuficientStockException extends RuntimeException {
    public InsuficientStockException(String message) {
        super(message);
    }
}
