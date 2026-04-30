package com.gft.warehouse.warehouseworkshop;

import org.springframework.boot.SpringApplication;

public class TestWarehouseWorkshopApplication {

    public static void main(String[] args) {
        SpringApplication.from(WarehouseWorkshopApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
