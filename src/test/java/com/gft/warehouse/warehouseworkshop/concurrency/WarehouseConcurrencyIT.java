package com.gft.warehouse.warehouseworkshop.concurrency;

import com.gft.warehouse.warehouseworkshop.application.dto.FactoryIdDTO;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.WarehouseJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Concurrency tests for warehouse operations.
 *
 * These tests verify that operations on shared state are safe under concurrent access.
 * They rely on a real HTTP stack (SpringBootTest.RANDOM_PORT) so that the servlet
 * container dispatches requests across multiple threads, mirroring production behaviour.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers
class WarehouseConcurrencyIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQ = new RabbitMQContainer("rabbitmq:3-management");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    WarehouseJpaRepository warehouseJpaRepository;

    @AfterEach
    void cleanup() {
        warehouseJpaRepository.deleteAll();
    }

    @Test
    void assignFactory_concurrent_onlyOneFactoryShouldSucceed() throws InterruptedException {
        UUID warehouseId = UUID.randomUUID();
        warehouseJpaRepository.save(WarehouseEntity.builder()
                .id(warehouseId)
                .name("Contested Warehouse")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0)
                .factoryId(null)
                .build());

        int threadCount = 10;
        CountDownLatch startGate = new CountDownLatch(1);   // holds all threads until we say go
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        List<String> responses = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            UUID factoryId = UUID.randomUUID();
            executor.submit(() -> {
                try {
                    startGate.await(); // all threads start at the same moment
                    ResponseEntity<String> response = restTemplate.exchange(
                            "/warehouses/assignFactory/{id}",
                            HttpMethod.PATCH,
                            new HttpEntity<>(FactoryIdDTO.builder().factoryId(factoryId.toString()).build()),
                            String.class,
                            warehouseId);
                    if (response.getBody() != null) {
                        responses.add(response.getBody());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startGate.countDown(); // release all threads simultaneously
        assertThat(doneLatch.await(30, TimeUnit.SECONDS))
                .as("All threads should finish within 30 s")
                .isTrue();
        executor.shutdown();

        long successCount = responses.stream()
                .filter(r -> r.contains("succesfully assigned"))
                .count();

        assertThat(successCount)
                .as("Exactly one factory should claim the warehouse; got %d successes. Responses: %s",
                        successCount, responses)
                .isEqualTo(1);

        assertThat(warehouseJpaRepository.findById(warehouseId).orElseThrow().getFactoryId())
                .as("Warehouse must have exactly one factoryId after the race")
                .isNotNull();
    }

    @Test
    void saveWarehouse_concurrent_allRowsMustBePersistedWithoutLoss() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        List<UUID> createdIds = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            UUID id = UUID.randomUUID();
            executor.submit(() -> {
                try {
                    startGate.await();
                    warehouseJpaRepository.save(WarehouseEntity.builder()
                            .id(id)
                            .name("Warehouse-" + id)
                            .warehouseType(WarehouseType.FACTORY)
                            .x(0).y(0)
                            .factoryId(null)
                            .build());
                    createdIds.add(id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startGate.countDown();
        assertThat(doneLatch.await(30, TimeUnit.SECONDS)).isTrue();
        executor.shutdown();

        assertThat(createdIds).hasSize(threadCount);
        createdIds.forEach(id ->
                assertThat(warehouseJpaRepository.findById(id))
                        .as("Warehouse %s must exist in DB", id)
                        .isPresent());
    }
}