package com.gft.warehouse.warehouseworkshop.integration;

import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.WarehouseJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers
class WarehouseRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    WarehouseJpaRepository repository;

    @Test
    void findAll_whenWarehousesExist_returnsAllEntities() {
        repository.save(entity("Central", WarehouseType.FACTORY, null));
        repository.save(entity("Norte", WarehouseType.PRODUCTION, null));

        List<WarehouseEntity> result = repository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_whenEntityExists_returnsItWithCorrectFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        repository.save(WarehouseEntity.builder()
                .id(id).name("Alpha")
                .warehouseType(WarehouseType.PRODUCTION)
                .x(5).y(8).factoryId(factoryId).build());

        Optional<WarehouseEntity> result = repository.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getName()).isEqualTo("Alpha");
        assertThat(result.get().getWarehouseType()).isEqualTo(WarehouseType.PRODUCTION);
        assertThat(result.get().getX()).isEqualTo(5);
        assertThat(result.get().getY()).isEqualTo(8);
        assertThat(result.get().getFactoryId()).isEqualTo(factoryId);
    }

    @Test
    void findById_whenEntityDoesNotExist_returnsEmpty() {
        Optional<WarehouseEntity> result = repository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void save_persistsAllFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();

        repository.save(WarehouseEntity.builder()
                .id(id).name("Beta")
                .warehouseType(WarehouseType.CLIENT)
                .x(3).y(7).factoryId(factoryId).build());

        WarehouseEntity found = repository.findById(id).orElseThrow();
        assertThat(found.getName()).isEqualTo("Beta");
        assertThat(found.getWarehouseType()).isEqualTo(WarehouseType.CLIENT);
        assertThat(found.getX()).isEqualTo(3);
        assertThat(found.getY()).isEqualTo(7);
        assertThat(found.getFactoryId()).isEqualTo(factoryId);
    }

    @Test
    void deleteById_removesEntityFromDatabase() {
        UUID id = UUID.randomUUID();
        repository.save(entity("ToDelete", WarehouseType.FACTORY, null));
        repository.save(WarehouseEntity.builder()
                .id(id).name("ToDelete2")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0).build());

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void findByWarehouseTypeAndFactoryIdNull_returnsOnlyFactoryTypeWithNoFactoryAssigned() {
        UUID availableId = UUID.randomUUID();
        repository.save(WarehouseEntity.builder()
                .id(availableId).name("Available")
                .warehouseType(WarehouseType.FACTORY)
                .x(1).y(1).factoryId(null).build());
        repository.save(WarehouseEntity.builder()
                .id(UUID.randomUUID()).name("Taken")
                .warehouseType(WarehouseType.FACTORY)
                .x(2).y(2).factoryId(UUID.randomUUID()).build());
        repository.save(WarehouseEntity.builder()
                .id(UUID.randomUUID()).name("Production")
                .warehouseType(WarehouseType.PRODUCTION)
                .x(3).y(3).factoryId(null).build());

        List<WarehouseEntity> result = repository.findByWarehouseTypeAndFactoryIdNull(WarehouseType.FACTORY);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(availableId);
        assertThat(result.getFirst().getFactoryId()).isNull();
    }

    @Test
    void findByWarehouseTypeAndFactoryIdNull_whenNoFactoryWithoutAssignment_returnsEmpty() {
        repository.save(WarehouseEntity.builder()
                .id(UUID.randomUUID()).name("Taken")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0).factoryId(UUID.randomUUID()).build());

        List<WarehouseEntity> result = repository.findByWarehouseTypeAndFactoryIdNull(WarehouseType.FACTORY);

        assertThat(result).isEmpty();
    }

    // ─── assignFactoryIdIfAvailable ──────────────────────────────────────────────

    @Test
    void assignFactoryIdIfAvailable_whenFactoryIdIsNull_updatesRowAndReturns1() {
        UUID warehouseId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        repository.save(WarehouseEntity.builder()
                .id(warehouseId).name("Available")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0).factoryId(null).build());

        int updated = repository.assignFactoryIdIfAvailable(warehouseId, factoryId);

        assertThat(updated).isEqualTo(1);
        assertThat(repository.findById(warehouseId).orElseThrow().getFactoryId()).isEqualTo(factoryId);
    }

    @Test
    void assignFactoryIdIfAvailable_whenFactoryIdAlreadySet_returns0AndDoesNotOverwrite() {
        UUID warehouseId = UUID.randomUUID();
        UUID existingFactoryId = UUID.randomUUID();
        repository.save(WarehouseEntity.builder()
                .id(warehouseId).name("AlreadyAssigned")
                .warehouseType(WarehouseType.FACTORY)
                .x(0).y(0).factoryId(existingFactoryId).build());

        int updated = repository.assignFactoryIdIfAvailable(warehouseId, UUID.randomUUID());

        assertThat(updated).isEqualTo(0);
        assertThat(repository.findById(warehouseId).orElseThrow().getFactoryId()).isEqualTo(existingFactoryId);
    }

    @Test
    void assignFactoryIdIfAvailable_whenWarehouseDoesNotExist_returns0() {
        int updated = repository.assignFactoryIdIfAvailable(UUID.randomUUID(), UUID.randomUUID());

        assertThat(updated).isEqualTo(0);
    }

    private WarehouseEntity entity(String name, WarehouseType type, UUID factoryId) {
        return WarehouseEntity.builder()
                .id(UUID.randomUUID()).name(name)
                .warehouseType(type).x(0).y(0)
                .factoryId(factoryId).build();
    }
}