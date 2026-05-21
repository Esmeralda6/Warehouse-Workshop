package com.gft.warehouse.warehouseworkshop.e2e;

import com.gft.warehouse.warehouseworkshop.application.dto.FactoryIdDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.WarehouseJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers
class WarehouseApiE2E {

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

    // ─── POST /warehouses ────────────────────────────────────────────────────────

    @Test
    void saveWarehouse_E2E_shouldPersistAndReturnIdInConfirmationMessage() {
        WarehouseDTO request = buildRequest("Main Warehouse", "FACTORY", 10, 20);

        ResponseEntity<String> response = restTemplate.postForEntity("/warehouses", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).startsWith("Warehouse saved with id:");

        UUID savedId = extractIdFromResponse(response.getBody());
        assertThat(warehouseJpaRepository.findById(savedId)).isPresent();
    }

    // ─── GET /warehouses ────────────────────────────────────────────────────

    @Test
    void getWarehouses_E2E_afterSavingTwo_shouldReturnBothInList() {
        warehouseJpaRepository.save(entity("Alpha", WarehouseType.FACTORY, null));
        warehouseJpaRepository.save(entity("Beta", WarehouseType.PRODUCTION, null));

        ResponseEntity<List> response = restTemplate.getForEntity("/warehouses", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void getWarehouses_E2E_whenDatabaseIsEmpty_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.getForEntity("/warehouses", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    // ─── GET /warehouses/{id} ────────────────────────────────────────────────────

    @Test
    void getWarehouseById_E2E_afterSaving_shouldReturnMatchingWarehouseWithAllFields() {
        String responseBody = restTemplate.postForObject("/warehouses",
                buildRequest("Sigma Warehouse", "PRODUCTION", 5, 8), String.class);
        UUID id = extractIdFromResponse(responseBody);

        ResponseEntity<WarehouseDTO> response = restTemplate.getForEntity(
                "/warehouses/{id}", WarehouseDTO.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Sigma Warehouse");
        assertThat(response.getBody().getType()).isEqualTo("PRODUCTION");
        assertThat(response.getBody().getLocation().getX()).isEqualTo(5);
        assertThat(response.getBody().getLocation().getY()).isEqualTo(8);
    }

    @Test
    void getWarehouseById_E2E_whenWarehouseDoesNotExist_shouldReturnJsonNull() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/warehouses/{id}", String.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Optional.empty() serializes as JSON null → body is the literal string "null"
        assertThat(response.getBody()).isEqualTo("null");
    }

    // ─── PUT /warehouses/{id} ────────────────────────────────────────────────────

    @Test
    void updateWarehouse_E2E_shouldModifyNameTypeAndLocationInDatabase() {
        UUID id = extractIdFromResponse(
                restTemplate.postForObject("/warehouses", buildRequest("Original", "FACTORY", 1, 1), String.class));

        WarehouseDTO updated = buildRequest("Updated", "PRODUCTION", 9, 9);
        restTemplate.put("/warehouses/{id}", updated, id);

        WarehouseEntity persisted = warehouseJpaRepository.findById(id).orElseThrow();
        assertThat(persisted.getName()).isEqualTo("Updated");
        assertThat(persisted.getWarehouseType()).isEqualTo(WarehouseType.PRODUCTION);
        assertThat(persisted.getX()).isEqualTo(9);
        assertThat(persisted.getY()).isEqualTo(9);
    }

    @Test
    void updateWarehouse_E2E_whenWarehouseDoesNotExist_shouldReturnNotFoundMessage() {
        String nonExistentId = UUID.randomUUID().toString();

        ResponseEntity<String> response = restTemplate.exchange(
                "/warehouses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(buildRequest("X", "FACTORY", 0, 0)),
                String.class,
                nonExistentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("not found");
    }

    // ─── DELETE /warehouses/{id} ─────────────────────────────────────────────────

    @Test
    void deleteWarehouse_E2E_shouldRemoveEntityFromDatabase() {
        UUID id = extractIdFromResponse(
                restTemplate.postForObject("/warehouses", buildRequest("ToDelete", "CLIENT", 3, 3), String.class));

        restTemplate.delete("/warehouses/{id}", id);

        assertThat(warehouseJpaRepository.findById(id)).isEmpty();
    }

    @Test
    void deleteWarehouse_E2E_whenWarehouseDoesNotExist_shouldReturnNotFoundMessage() {
        String nonExistentId = UUID.randomUUID().toString();

        ResponseEntity<String> response = restTemplate.exchange(
                "/warehouses/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class,
                nonExistentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("not found");
    }

    // ─── GET /warehouses/available ───────────────────────────────────────────────

    @Test
    void findAvailableWarehouse_E2E_whenFactoryTypeExistsWithNoFactoryAssigned_shouldReturnIt() {
        UUID id = UUID.randomUUID();
        warehouseJpaRepository.save(WarehouseEntity.builder()
                .id(id).name("Free Factory Warehouse")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0).factoryId(null).build());

        ResponseEntity<WarehouseDTO> response = restTemplate.getForEntity("/warehouses/available", WarehouseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id.toString());
        assertThat(response.getBody().getFactoryId()).isNull();
    }

    @Test
    void findAvailableWarehouse_E2E_whenAllFactoryWarehousesAreAssigned_shouldReturnJsonNull() {
        warehouseJpaRepository.save(WarehouseEntity.builder()
                .id(UUID.randomUUID()).name("Taken")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0).factoryId(UUID.randomUUID()).build());

        ResponseEntity<String> response = restTemplate.getForEntity("/warehouses/available", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Optional.empty() serializes as JSON null → body is the literal string "null"
        assertThat(response.getBody()).isEqualTo("null");
    }

    // ─── PATCH /warehouses/assignFactory/{id} ────────────────────────────────────

    @Test
    void assignFactory_E2E_shouldPersistFactoryIdInWarehouse() {
        UUID warehouseId = UUID.randomUUID();
        warehouseJpaRepository.save(WarehouseEntity.builder()
                .id(warehouseId).name("Assignable")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0).factoryId(null).build());
        UUID newFactoryId = UUID.randomUUID();

        ResponseEntity<String> response = restTemplate.exchange(
                "/warehouses/assignFactory/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(FactoryIdDTO.builder().factoryId(newFactoryId.toString()).build()),
                String.class,
                warehouseId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("succesfully assigned");

        WarehouseEntity updated = warehouseJpaRepository.findById(warehouseId).orElseThrow();
        assertThat(updated.getFactoryId()).isEqualTo(newFactoryId);
    }

    @Test
    void assignFactory_E2E_whenWarehouseDoesNotExist_shouldReturnNotFoundMessage() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/warehouses/assignFactory/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(FactoryIdDTO.builder().factoryId(UUID.randomUUID().toString()).build()),
                String.class,
                UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("not found");
    }

    // ─── Lifecycle scenarios ─────────────────────────────────────────────────────

    @Test
    void fullCrudLifecycle_E2E_createReadUpdateDelete_stateIsCoherentAcrossAllOperations() {
        // CREATE
        UUID id = extractIdFromResponse(
                restTemplate.postForObject("/warehouses", buildRequest("Lifecycle", "FACTORY", 5, 5), String.class));

        // READ — debe encontrarlo con los datos originales
        WarehouseDTO created = restTemplate.getForEntity("/warehouses/{id}", WarehouseDTO.class, id).getBody();
        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Lifecycle");
        assertThat(created.getType()).isEqualTo("FACTORY");

        // UPDATE
        restTemplate.put("/warehouses/{id}", buildRequest("Lifecycle Updated", "CLIENT", 9, 9), id);

        // READ — debe reflejar los cambios
        WarehouseDTO updated = restTemplate.getForEntity("/warehouses/{id}", WarehouseDTO.class, id).getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("Lifecycle Updated");
        assertThat(updated.getType()).isEqualTo("CLIENT");

        // DELETE
        restTemplate.delete("/warehouses/{id}", id);

        // READ — ya no debe existir
        ResponseEntity<String> afterDelete = restTemplate.getForEntity("/warehouses/{id}", String.class, id);
        assertThat(afterDelete.getBody()).isEqualTo("null");
    }

    @Test
    void assignFactory_E2E_afterAssignment_warehouseShouldNoLongerAppearAsAvailable() {
        // Crear un warehouse FACTORY sin factory asignada (aparece en /available)
        UUID warehouseId = UUID.randomUUID();
        warehouseJpaRepository.save(WarehouseEntity.builder()
                .id(warehouseId).name("Will Be Taken")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0).factoryId(null).build());

        // Verificar que aparece como disponible
        ResponseEntity<WarehouseDTO> before = restTemplate.getForEntity("/warehouses/available", WarehouseDTO.class);
        assertThat(before.getBody()).isNotNull();
        assertThat(before.getBody().getId()).isEqualTo(warehouseId.toString());

        // Asignar factory
        UUID factoryId = UUID.randomUUID();
        restTemplate.exchange(
                "/warehouses/assignFactory/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(FactoryIdDTO.builder().factoryId(factoryId.toString()).build()),
                String.class,
                warehouseId);

        // Verificar que ya no aparece como disponible
        ResponseEntity<String> after = restTemplate.getForEntity("/warehouses/available", String.class);
        assertThat(after.getBody()).isEqualTo("null");
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    private WarehouseDTO buildRequest(String name, String type, int x, int y) {
        return WarehouseDTO.builder()
                .name(name).type(type)
                .location(LocationDTO.builder().x(x).y(y).build())
                .build();
    }

    private WarehouseEntity entity(String name, WarehouseType type, UUID factoryId) {
        return WarehouseEntity.builder()
                .id(UUID.randomUUID()).name(name)
                .warehouseType(type).x(0).y(0)
                .factoryId(factoryId).build();
    }

    private UUID extractIdFromResponse(String body) {
        return UUID.fromString(body.replace("Warehouse saved with id: ", "").trim());
    }
}