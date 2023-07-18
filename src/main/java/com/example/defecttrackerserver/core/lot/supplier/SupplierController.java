package com.example.defecttrackerserver.core.lot.supplier;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping
    public SupplierDto saveProcess(@Valid @RequestBody SupplierDto supplierDto) {
        return supplierService.saveSupplier(supplierDto);
    }

    @GetMapping("/{id}")
    public SupplierDto getProcess(@PathVariable Integer id) {
        return supplierService.getSupplierById(id);
    }

    @GetMapping
    public List<SupplierDto> getAllProcesses() {
        return supplierService.getAllSuppliers();
    }

    @PutMapping
    public SupplierDto updateProcess(@Valid@RequestBody SupplierDto supplierDto) {
        return supplierService.updateSupplier(supplierDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProcess(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
    }
}
