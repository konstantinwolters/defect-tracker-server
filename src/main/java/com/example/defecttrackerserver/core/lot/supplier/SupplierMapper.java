package com.example.defecttrackerserver.core.lot.supplier;

import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierDto mapToDto(Supplier supplier) {
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(supplier.getId());
        supplierDto.setName(supplier.getName());
        return supplierDto;
    }
}
