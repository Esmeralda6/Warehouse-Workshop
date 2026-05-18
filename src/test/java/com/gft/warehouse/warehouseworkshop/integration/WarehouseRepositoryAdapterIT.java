package com.gft.warehouse.warehouseworkshop.integration;

import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.repository.WarehouseRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
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
@Import(WarehouseRepositoryAdapter.class)
@Testcontainers
class WarehouseRepositoryAdapterIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    WarehouseRepositoryAdapter adapter;

    // ─── findAll ─────────────────────────────────────────────────────────────────

    @Test
    void findAll_returnsDomainObjects_withCorrectlyMappedFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        adapter.save(warehouse(id, "Central", WarehouseType.FACTORY, 10, 20, factoryId));

        List<Warehouse> result = adapter.findAll();

        assertThat(result).hasSize(1);
        Warehouse found = result.getFirst();
        assertThat(found).isInstanceOf(Warehouse.class);
        assertThat(found.getWarehouseId().getId()).isEqualTo(id);
        assertThat(found.getWarehouseName()).isEqualTo("Central");
        assertThat(found.getWarehouseType()).isEqualTo(WarehouseType.FACTORY);
        assertThat(found.getWarehouseLocation().getX()).isEqualTo(10);
        assertThat(found.getWarehouseLocation().getY()).isEqualTo(20);
        assertThat(found.getFactoryId().getId()).isEqualTo(factoryId);
    }

    // ─── findById ────────────────────────────────────────────────────────────────

    @Test
    void findById_whenExists_returnsDomainObjectWithAllFieldsMapped() {
        UUID id = UUID.randomUUID();
        adapter.save(warehouse(id, "Norte", WarehouseType.PRODUCTION, 5, 8, null));

        Optional<Warehouse> result = adapter.findById(WarehouseId.builder().id(id).build());

        assertThat(result).isPresent();
        assertThat(result.get().getWarehouseId().getId()).isEqualTo(id);
        assertThat(result.get().getWarehouseName()).isEqualTo("Norte");
        assertThat(result.get().getWarehouseType()).isEqualTo(WarehouseType.PRODUCTION);
        assertThat(result.get().getWarehouseLocation().getX()).isEqualTo(5);
        assertThat(result.get().getWarehouseLocation().getY()).isEqualTo(8);
        // factoryId should be mapped even when null
        assertThat(result.get().getFactoryId()).isNotNull();
        assertThat(result.get().getFactoryId().getId()).isNull();
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        Optional<Warehouse> result = adapter.findById(WarehouseId.builder().id(UUID.randomUUID()).build());

        assertThat(result).isEmpty();
    }

    // ─── save ────────────────────────────────────────────────────────────────────

    @Test
    void save_persistsDomainObjectAndReturnsEntityWithSameId() {
        UUID id = UUID.randomUUID();
        Warehouse toSave = warehouse(id, "Sur", WarehouseType.CLIENT, 3, 7, UUID.randomUUID());

        WarehouseEntity savedEntity = adapter.save(toSave);

        assertThat(savedEntity.getId()).isEqualTo(id);

        // verify it's actually in DB via findById
        Warehouse reloaded = adapter.findById(WarehouseId.builder().id(id).build()).orElseThrow();
        assertThat(reloaded.getWarehouseName()).isEqualTo("Sur");
        assertThat(reloaded.getWarehouseType()).isEqualTo(WarehouseType.CLIENT);
        assertThat(reloaded.getWarehouseLocation().getX()).isEqualTo(3);
        assertThat(reloaded.getWarehouseLocation().getY()).isEqualTo(7);
    }

    // ─── delete ──────────────────────────────────────────────────────────────────

    @Test
    void delete_removesWarehouseFromDatabase() {
        UUID id = UUID.randomUUID();
        adapter.save(warehouse(id, "ToDelete", WarehouseType.FACTORY, 0, 0, null));

        adapter.delete(WarehouseId.builder().id(id).build());

        assertThat(adapter.findById(WarehouseId.builder().id(id).build())).isEmpty();
    }

    // ─── findAvailable ───────────────────────────────────────────────────────────

    @Test
    void findAvailable_whenFactoryTypeExistsWithNoFactoryId_returnsMappedDomainObject() {
        UUID availableId = UUID.randomUUID();
        adapter.save(warehouse(availableId, "Free", WarehouseType.FACTORY, 1, 1, null));
        adapter.save(warehouse(UUID.randomUUID(), "Taken", WarehouseType.FACTORY, 2, 2, UUID.randomUUID()));
        adapter.save(warehouse(UUID.randomUUID(), "Prod", WarehouseType.PRODUCTION, 3, 3, null));

        Optional<Warehouse> result = adapter.findAvailable();

        assertThat(result).isPresent();
        assertThat(result.get().getWarehouseId().getId()).isEqualTo(availableId);
        assertThat(result.get().getWarehouseType()).isEqualTo(WarehouseType.FACTORY);
        assertThat(result.get().getFactoryId().getId()).isNull();
    }

    @Test
    void findAvailable_whenNoFactoryTypeWithoutFactoryId_returnsEmpty() {
        adapter.save(warehouse(UUID.randomUUID(), "Taken", WarehouseType.FACTORY, 0, 0, UUID.randomUUID()));

        Optional<Warehouse> result = adapter.findAvailable();

        assertThat(result).isEmpty();
    }

    // ─── Helper ──────────────────────────────────────────────────────────────────

    private Warehouse warehouse(UUID id, String name, WarehouseType type, int x, int y, UUID factoryId) {
        return Warehouse.builder()
                .warehouseId(WarehouseId.builder().id(id).build())
                .warehouseName(name)
                .warehouseType(type)
                .warehouseLocation(Location.builder().x(x).y(y).build())
                .factoryId(FactoryId.builder().id(factoryId).build())
                .build();
    }
}