package com.gft.warehouse.warehouseworkshop.application.service;

import com.gft.warehouse.warehouseworkshop.application.dto.LocationDTO;
import com.gft.warehouse.warehouseworkshop.application.dto.WarehouseDTO;
import com.gft.warehouse.warehouseworkshop.domain.aggregates.Warehouse;
import com.gft.warehouse.warehouseworkshop.domain.enums.Type;
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

    // GET warehouses
    public List<WarehouseDTO> getWarehouses(){
        return warehouseRepository.findAll()
                .stream()
                .map(Mapper::toDTO)
                .toList();
    }

    //GET By Id
    public Optional<WarehouseDTO> getWarehouseById( String id){
        return warehouseRepository.findById(WarehouseId.builder().id(UUID.fromString(id)).build())
                .map(Mapper::toDTO);
    }


    public String saveWarehouse(WarehouseDTO warehouseDTO) {
        log.info(warehouseDTO.toString());
        WarehouseEntity savedWarehouse = warehouseRepository.save(
                Mapper.toDomain(warehouseDTO)
        );
        log.info(String.valueOf(savedWarehouse.getFactoryId()==null));

        return "Warehouse saved with id: " + savedWarehouse.getId().toString();
    }

    public String updateWarehouse(String id, WarehouseDTO warehouseDTO) {
        log.info(warehouseDTO.toString());
        Optional<WarehouseDTO> warehouse = warehouseRepository.findById(
                WarehouseId.builder().id(UUID.fromString(id)).build())
                .map(updatedWarehouse -> {
                    updatedWarehouse.setWarehouseName( warehouseDTO.getName() );
                    updatedWarehouse.setWarehouseType( Type.valueOf(warehouseDTO.getType()) );
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
                    log.info(updatedWarehouse.toString());
                    return Mapper.toDto( warehouseRepository.save(updatedWarehouse) );
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

    public String findAvailableWarehouse(FactoryId factoryId) {

        return "";
    }
}
