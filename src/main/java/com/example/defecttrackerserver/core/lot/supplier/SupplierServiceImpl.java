package com.example.defecttrackerserver.core.lot.supplier;

import com.example.defecttrackerserver.core.lot.supplier.supplierException.SupplierExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_PURCHASER') or hasRole('ROLE_ADMIN')")
    public SupplierDto saveSupplier(SupplierDto supplierDto) {
        if(supplierDto.getName() == null)
            throw new IllegalArgumentException("Supplier name must not be null");

        if(supplierRepository.findByName(supplierDto.getName()).isPresent())
            throw new SupplierExistsException("Supplier already exists with name: " + supplierDto.getName());

        Supplier supplier = new Supplier();
        supplier.setName(supplierDto.getName());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.mapToDto(savedSupplier);
    }

    @Override
    public SupplierDto getSupplierById(Integer id) {
        return supplierRepository.findById(id)
                .map(supplierMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_PURCHASER') or hasRole('ROLE_ADMIN')")
    public SupplierDto updateSupplier(SupplierDto supplierDto) {
        if(supplierDto.getId() == null)
            throw new IllegalArgumentException("Supplier id must not be null");
        if(supplierDto.getName() == null)
            throw new IllegalArgumentException("Supplier name must not be null");

        Supplier supplier = supplierRepository.findById(supplierDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Supplier not found with id: "
                        + supplierDto.getId()));

        Optional<Supplier> supplierExists = supplierRepository.findByName(supplierDto.getName());
        if(supplierExists.isPresent() && !supplierExists.get().getId().equals(supplier.getId()))
            throw new SupplierExistsException("Supplier already exists with name: " + supplierDto.getName());

        supplier.setName(supplierDto.getName());
        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.mapToDto(savedSupplier);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteSupplier(Integer id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Supplier not found with id: " + id));

        supplierRepository.delete(supplier);
    }
}
