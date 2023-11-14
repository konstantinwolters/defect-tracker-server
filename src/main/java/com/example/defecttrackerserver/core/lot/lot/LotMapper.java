package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import com.example.defecttrackerserver.core.lot.material.MaterialMapper;
import com.example.defecttrackerserver.core.lot.supplier.SupplierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
@RequiredArgsConstructor
public class LotMapper {
    private final EntityService entityService;
    private final MaterialMapper materialMapper;
    private final SupplierMapper supplierMapper;

    public Lot map(LotDto lotDto, Lot lot){
        lot.setLotNumber(lotDto.getLotNumber());
        lot.setMaterial(entityService.getMaterialById(lotDto.getMaterial().getId()));
        lot.setSupplier(entityService.getSupplierById(lotDto.getSupplier().getId()));

        if(lotDto.getDefects() != null) {
            Set<Defect> defects = lotDto.getDefects().stream()
                    .map(entityService::getDefectById)
                    .collect(Collectors.toSet());
            lot.getDefects().clear();
            defects.forEach(lot::addDefect);
        }
        return lot;
    }

    public LotDto mapToDto(Lot lot){
        LotDto lotDto = new LotDto();
        lotDto.setId(lot.getId());
        lotDto.setLotNumber(lot.getLotNumber());
        lotDto.setMaterial(materialMapper.mapToDto(lot.getMaterial()));
        lotDto.setSupplier(supplierMapper.mapToDto(lot.getSupplier()));
        lotDto.setDefects(lot.getDefects().stream()
                .map(Defect::getId)
                .collect(Collectors.toSet()));
        return lotDto;
    }
}
