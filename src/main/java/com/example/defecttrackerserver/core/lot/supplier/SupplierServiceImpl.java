package com.example.defecttrackerserver.core.lot.supplier;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SupplierService}.
 */
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_PURCHASER') or hasRole('ROLE_QA') or hasRole('ROLE_ADMIN') ")
    public SupplierDto saveSupplier(SupplierDto supplierDto) {

        if(supplierRepository.findByName(supplierDto.getName()).isPresent())
            throw new DuplicateKeyException("Supplier already exists with name: " + supplierDto.getName());

        Supplier supplier = new Supplier();
        supplier.setName(supplierDto.getName());
        supplier.setCustomId(supplierDto.getCustomId());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.mapToDto(savedSupplier);
    }

    @Override
    public SupplierDto getSupplierById(Integer id) {
        Supplier supplier = findSupplierById(id);
        return supplierMapper.mapToDto(supplier);
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
    @PreAuthorize("hasRole('ROLE_QA') or hasRole('ROLE_ADMIN')")
    public SupplierDto updateSupplier(Integer supplierId, SupplierDto supplierDto) {

        Supplier supplier = findSupplierById(supplierId);

        Optional<Supplier> supplierExists = supplierRepository.findByName(supplierDto.getName());
        if(supplierExists.isPresent() && !supplierExists.get().getId().equals(supplier.getId()))
            throw new DuplicateKeyException("Supplier already exists with name: " + supplierDto.getName());

        supplier.setName(supplierDto.getName());
        supplier.setCustomId(supplierDto.getCustomId());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.mapToDto(savedSupplier);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteSupplier(Integer id) {
        Supplier supplier = findSupplierById(id);

        supplierRepository.delete(supplier);
    }

    private Supplier findSupplierById(Integer id){
        return supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }
}
