package com.example.defecttrackerserver.core.lot.supplier;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierDto saveSupplier(SupplierDto materialDto) {
        if(materialDto.getName() == null)
            throw new IllegalArgumentException("Supplier name must not be null");

        Supplier supplier = new Supplier();
        supplier.setName(materialDto.getName());

        Supplier savedMaterial = supplierRepository.save(supplier);

        return supplierMapper.mapToDto(savedMaterial);
    }

    @Override
    public SupplierDto getSupplierById(Integer id) {
        return supplierRepository.findById(id)
                .map(supplierMapper::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDto updateSupplier(SupplierDto materialDto) {
        if(materialDto.getId() == null)
            throw new IllegalArgumentException("Supplier id must not be null");
        if(materialDto.getName() == null)
            throw new IllegalArgumentException("Supplier name must not be null");

        Supplier supplier = supplierRepository.findById(materialDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Supplier not found with id: "
                        + materialDto.getId()));

        supplier.setName(materialDto.getName());
        Supplier savedMaterial = supplierRepository.save(supplier);

        return supplierMapper.mapToDto(savedMaterial);
    }

    @Override
    public void deleteSupplier(Integer id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Supplier not found with id: " + id));

        supplierRepository.delete(supplier);
    }
}
