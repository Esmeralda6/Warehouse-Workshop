package com.gft.warehouse.warehouseworkshop.domain.services;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;

import java.util.List;

public interface StockChecker {
    boolean checkOwnStock(List<StockItem> warehouseStock, List<StockItem> requestedItems);
}