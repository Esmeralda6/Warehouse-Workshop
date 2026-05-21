package com.gft.warehouse.warehouseworkshop.contract;

import com.gft.warehouse.warehouseworkshop.application.dto.FactoryIdDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.application.service.warehouse.WarehouseService;
import com.gft.warehouse.warehouseworkshop.infrastructure.rest.WarehouseController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

/**
 * Base class for all Spring Cloud Contract generated tests.
 *
 * The plugin reads YAML contracts from src/test/resources/contracts/ and generates
 * a JUnit test class per folder. Each generated class extends this base class.
 *
 * WHY standalone MockMvc:
 *   Contract tests verify the HTTP contract (paths, JSON shape, status codes),
 *   not business logic or persistence. A standalone setup is faster, requires no
 *   database or RabbitMQ, and keeps contract tests isolated from infrastructure.
 *
 * Fixed UUIDs are used so the YAML contracts can reference concrete values that
 * the service mock will match exactly.
 */
public abstract class ContractBase {

    static final String WAREHOUSE_ID = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";
    static final String FACTORY_ID   = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb";
    static final String MISSING_ID   = "cccccccc-cccc-cccc-cccc-cccccccccccc";

    @BeforeEach
    void setup() {
        WarehouseService warehouseService = Mockito.mock(WarehouseService.class);
        stubService(warehouseService);

        WarehouseController controller = new WarehouseController();
        ReflectionTestUtils.setField(controller, "warehouseService", warehouseService);
        RestAssuredMockMvc.standaloneSetup(controller);
    }

    private void stubService(WarehouseService warehouseService) {
        WarehouseDTO sample = WarehouseDTO.builder()
                .id(WAREHOUSE_ID)
                .name("Central Warehouse")
                .type("FACTORY")
                .location(LocationDTO.builder().x(10).y(20).build())
                .factoryId(null)
                .build();

        Mockito.when(warehouseService.getWarehouses())
                .thenReturn(List.of(sample));

        Mockito.when(warehouseService.getWarehouseById(WAREHOUSE_ID))
                .thenReturn(Optional.of(sample));

        Mockito.when(warehouseService.getWarehouseById(MISSING_ID))
                .thenReturn(Optional.empty());

        Mockito.when(warehouseService.saveWarehouse(Mockito.any()))
                .thenReturn("Warehouse saved with id: " + WAREHOUSE_ID);

        Mockito.when(warehouseService.deleteWarehouse(WAREHOUSE_ID))
                .thenReturn("Warehouse with id " + WAREHOUSE_ID + " succesfully deleted.");

        Mockito.when(warehouseService.deleteWarehouse(MISSING_ID))
                .thenReturn("Warehouse with id " + MISSING_ID + " was not found.");

        Mockito.when(warehouseService.findAvailableWarehouse())
                .thenReturn(Optional.of(sample));

        Mockito.when(warehouseService.assignFactoryId(
                        Mockito.eq(WAREHOUSE_ID), Mockito.any(FactoryIdDTO.class)))
                .thenReturn("Warehouse with id " + WAREHOUSE_ID
                        + " succesfully assigned to factory with id " + FACTORY_ID);

        Mockito.when(warehouseService.assignFactoryId(
                        Mockito.eq(MISSING_ID), Mockito.any(FactoryIdDTO.class)))
                .thenReturn("Warehouse with id " + MISSING_ID + " not found.");
    }
}