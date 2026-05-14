package com.gft.warehouse.warehouseworkshop.application.service.warehouse;

import com.gft.warehouse.warehouseworkshop.application.dto.FactoryIdDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.StockItemDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.WarehouseType;
import com.gft.warehouse.warehouseworkshop.domain.events.WarehouseCreatedEvent;
import com.gft.warehouse.warehouseworkshop.domain.ports.EventPublisher;
import com.gft.warehouse.warehouseworkshop.domain.repository.WarehouseRepository;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.FactoryId;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.Location;
import com.gft.warehouse.warehouseworkshop.domain.valueObject.WarehouseId;
import com.gft.warehouse.warehouseworkshop.infrastructure.persistence.entity.WarehouseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService{

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EventPublisher eventPublisher;

    // GET warehouses
    public List<WarehouseDTO> getWarehouses(){
        return warehouseRepository.findAll()
                .stream()
                .map(WarehouseMapperUtils::toDTO)
                .toList();
    }

    //GET By Id
    public Optional<WarehouseDTO> getWarehouseById( String id){
        return warehouseRepository.findById(WarehouseId.builder().id(UUID.fromString(id)).build())
                .map(WarehouseMapperUtils::toDTO);
    }


    public String saveWarehouse(WarehouseDTO warehouseDTO) {

        if ( warehouseDTO.getId() == null || warehouseDTO.getId().isBlank() )
            warehouseDTO = WarehouseDTO.builder()
                    .id( UUID.randomUUID().toString() )
                    .name( warehouseDTO.getName() )
                    .type( warehouseDTO.getType() )
                    .location( warehouseDTO.getLocation() )
                    .factoryId( warehouseDTO.getFactoryId() )
                    .build();

        Warehouse warehouse = WarehouseMapperUtils.toDomain(warehouseDTO);

        try {
            warehouse.recordEvent(new WarehouseCreatedEvent(
                    warehouse.getWarehouseId().getId().toString(),
                    warehouse.getWarehouseName(),
                    warehouse.getWarehouseLocation(),
                    warehouse.getWarehouseType().name()
            ));
            warehouse.getDomainEvents().forEach(eventPublisher::publish);
            warehouse.clearDomainEvents();
        }catch (Exception e){
            log.error(e.getMessage());
        }

      
        WarehouseEntity savedWarehouse = warehouseRepository.save(
                warehouse
        );
        return "Warehouse saved with id: " + savedWarehouse.getId().toString();
    }

    public String updateWarehouse(String id, WarehouseDTO warehouseDTO) {
        Optional<WarehouseDTO> warehouse = warehouseRepository.findById(
                WarehouseId.builder().id(UUID.fromString(id)).build())
                .map(updatedWarehouse -> {
                    updatedWarehouse.setWarehouseName( warehouseDTO.getName() );
                    updatedWarehouse.setWarehouseType( WarehouseType.valueOf(warehouseDTO.getType()) );
                    updatedWarehouse.setWarehouseLocation(
                            Location.builder()
                                    .x(warehouseDTO.getLocation().getX())
                                    .y(warehouseDTO.getLocation().getY())
                                    .build()
                    );
                    if(warehouseDTO.getFactoryId() == null || warehouseDTO.getFactoryId().isBlank()){
                        updatedWarehouse.setFactoryId(
                                FactoryId.builder()
                                        .id(null)
                                        .build() );
                    }else{
                        updatedWarehouse.setFactoryId(
                                FactoryId.builder()
                                        .id(UUID.fromString(warehouseDTO.getFactoryId()))
                                        .build() );
                    }
                    return WarehouseMapperUtils.toDto( warehouseRepository.save(updatedWarehouse) );
                });
        if (warehouse.isPresent()){
            return "Warehouse with id " + id + " succesfully updated";
        }
        return "Warehouse with id " + id + " not found.";
    }

    public String deleteWarehouse(String id) {
        WarehouseId warehouseId = WarehouseId.builder().id(UUID.fromString(id)).build();
        if(
                warehouseRepository.findById( warehouseId ).isPresent()
        ){
            warehouseRepository.delete( warehouseId );
            return "Warehouse with id " + id + " succesfully deleted.";
        }
        return "Warehouse with id " + id + " was not found.";
    }

    //Factory methods
    public Optional<WarehouseDTO> findAvailableWarehouse() {
        return warehouseRepository.findAvailable().map(WarehouseMapperUtils::toDTO);
    }

    public String assignFactoryId( String warehouseId, FactoryIdDTO factoryId){
        Optional<WarehouseDTO> warehouse = warehouseRepository.findById(
                WarehouseId.builder().id(UUID.fromString(warehouseId)).build())
                .map(updatedWarehouse -> {
                    updatedWarehouse.setFactoryId(
                            FactoryId.builder().id( UUID.fromString( factoryId.getFactoryId() ) ).build());
                    return WarehouseMapperUtils.toDto( warehouseRepository.save(updatedWarehouse) );
                });
        if (warehouse.isPresent()){
            return "Warehouse with id " + warehouseId + " succesfully assigned to factory with id " + factoryId;
        }
        return "Warehouse with id " + warehouseId + " not found.";

    }

}
