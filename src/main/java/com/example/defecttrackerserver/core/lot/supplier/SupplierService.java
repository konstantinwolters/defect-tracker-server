package com.example.defecttrackerserver.core.lot.supplier;

import java.util.List;

public interface SupplierService {
    SupplierDto saveSupplier(SupplierDto supplierDto);
    SupplierDto getSupplierById(Integer id);
    List<SupplierDto> getAllSuppliers();
    SupplierDto updateSupplier(SupplierDto supplierDto);
    void deleteSupplier(Integer id);
}
