package com.gft.warehouse.warehouseworkshop.domain.aggregates;

import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import org.junit.jupiter.api.Test;


import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseTest {
    //Generate Warehouse
    @Test
    public void generateWarehouse(){
        Warehouse warehouse = Warehouse.builder()
                .warehouseId( WarehouseId.builder().id("id_1").build() )
                .warehouseName("Warehouse_1")
                .warehouseType( Type.PRODUCTION )
                .warehouseLocation( Location.builder().x(1).y(1).build())
                .minimumStockRules( Map.of(1,100))
                .isStockInfinite(true)
                .build();

        assertThat( warehouse).isInstanceOf( Warehouse.class);
        assertThat( warehouse.getWarehouseId() ).isInstanceOf(WarehouseId.class).isNotNull();
        assertThat( warehouse.getWarehouseName() ).isInstanceOf(String.class).isNotNull();
        assertThat( warehouse.getWarehouseType() ).isInstanceOf(Type.class).isNotNull();
        assertThat( warehouse.getWarehouseLocation() ).isInstanceOf(Location.class).isNotNull();
        assertThat( warehouse.getMinimumStockRules() ).isInstanceOf(Map.class).isNotNull();
        assertThat( warehouse.isStockInfinite() ).isInstanceOf(Boolean.class).isNotNull();

    }

}