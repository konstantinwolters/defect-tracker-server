package com.example.defecttrackerserver.core.lot.supplier;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public SupplierDto saveSupplier(SupplierDto materialDto) {
        if(materialDto.getName() == null)
            throw new IllegalArgumentException("Supplier name must not be null");

        Supplier supplier = new Supplier();
        supplier.setName(materialDto.getName());

        Supplier savedMaterial = supplierRepository.save(supplier);

        return modelMapper.map(savedMaterial, SupplierDto.class);
    }

    @Override
    public SupplierDto getSupplierById(Integer id) {
        return supplierRepository.findById(id)
                .map(supplier -> modelMapper.map(supplier, SupplierDto.class))
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplier -> modelMapper.map(supplier, SupplierDto.class))
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

        return modelMapper.map(savedMaterial, SupplierDto.class);
    }

    @Override
    public void deleteSupplier(Integer id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Supplier not found with id: " + id));

        supplierRepository.delete(supplier);
    }
}
