package com.gft.warehouse.warehouseworkshop.application.service;

import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.ports.EventPublisher;
import com.gft.warehouse.warehouseworkshop.domain.repository.WarehouseRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseServiceImpl implements WarehouseService{

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EventPublisher eventPublisher;

    // GET warehouses
    public List<WarehouseDTO> getWarehouses(){
        return warehouseRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    //GET By Id
        //prob wont work until changing the objectvalues euqalsandhascode() or annotated with @value or object as record instead of class
    public Optional<WarehouseDTO> getWarehouseById( String id){
        return warehouseRepository.findById(WarehouseId.builder().id(UUID.fromString(id)).build())
                .map(this::toDTO);
    }


    public String saveWarehouse(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = toDomain(warehouseDTO);
        warehouseRepository.save(warehouse);

        warehouse.recordEvent(new WarehouseCreatedEvent(
                warehouse.getWarehouseId().getId().toString(),
                warehouse.getWarehouseName(),
                warehouse.getWarehouseLocation(),
                warehouse.getWarehouseType().name()
        ));
        warehouse.getDomainEvents().forEach(eventPublisher::publish);
        warehouse.clearDomainEvents();

        return "Warehouse saved with id: " + warehouse.getWarehouseId().getId().toString();
    }

    //MAPPERS

    private WarehouseDTO toDTO(Warehouse warehouse){
        return WarehouseDTO.builder()
                .id( warehouse.getWarehouseId().getId().toString())
                .name( warehouse.getWarehouseName() )
                .location(
                        LocationDTO.builder()
                                .x(1)
                                .y(2)
                                .build())
                .type( String.valueOf(warehouse.getWarehouseType()) )
                .build();
    }

    private Warehouse toDomain(WarehouseDTO warehouseDTO){
        UUID id = warehouseDTO.getId() != null ?
                    UUID.fromString(warehouseDTO.getId())
                    : UUID.randomUUID();
        return Warehouse.builder()
                .warehouseId(
                        WarehouseId.builder()
                                .id(id)
                                .build()
                )
                .warehouseName( warehouseDTO.getName() )
                .warehouseLocation(
                        Location.builder()
                                .x(warehouseDTO.getLocation().getX())
                                .y(warehouseDTO.getLocation().getY())
                                .build()
                )
                .warehouseType( Type.valueOf( warehouseDTO.getType().toUpperCase() ) )
                .build();
    }
}
