package com.example.defecttrackerserver.core.lot.supplier;

import org.springframework.stereotype.Component;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
public class SupplierMapper {
    public SupplierDto mapToDto(Supplier supplier) {
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(supplier.getId());
        supplierDto.setName(supplier.getName());
        supplierDto.setCustomId(supplier.getCustomId());
        return supplierDto;
    }
}
