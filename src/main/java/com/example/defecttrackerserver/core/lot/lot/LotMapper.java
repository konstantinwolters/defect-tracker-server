package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LotMapper {
    private final DefectRepository defectRepository;
    private final MaterialRepository materialRepository;
    private final SupplierRepository supplierRepository;

    public Lot map(LotDto lotDto, Lot lot){
        checkNullOrEmptyFields(lotDto);
        lot.setLotNumber(lotDto.getLotNumber());

        lot.setMaterial(materialRepository.findById(lotDto.getMaterial().getId())
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: "
                        + lotDto.getMaterial().getId())));

        lot.setSupplier(supplierRepository.findById(lotDto.getSupplier().getId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: "
                        + lotDto.getSupplier().getId())));

        if(lotDto.getDefects() != null) {
            Set<Defect> defects = lotDto.getDefects().stream()
                    .map(defectId -> defectRepository.findById(defectId)
                            .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: "
                                    + defectId)))
                    .collect(Collectors.toSet());
            lot.getDefects().clear();
            defects.forEach(lot::addDefect);
        }
        return lot;
    }

    public void checkNullOrEmptyFields(LotDto lotDto) {
        if(lotDto.getLotNumber() == null)
            throw new IllegalArgumentException("Lot number cannot be null");

        if(lotDto.getMaterial() == null)
            throw new IllegalArgumentException("Material cannot be null");

        if(lotDto.getSupplier() == null)
            throw new IllegalArgumentException("Supplier cannot be null");
    }
}
