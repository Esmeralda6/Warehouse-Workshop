package com.gft.warehouse.warehouseworkshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class WarehouseWorkshopApplicationTests {

    @Test
    void contextLoads() {
    }

}
