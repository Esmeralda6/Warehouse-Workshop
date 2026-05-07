package com.gft.warehouse.warehouseworkshop.domain.services;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.StockItem;

import java.util.List;
import java.util.Map;

public interface ReplenishmentPolicy {
    boolean shouldReplenish(List<StockItem> stock, Map minStockRules);
}