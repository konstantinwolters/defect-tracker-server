package com.example.defecttrackerserver.core.lot.supplier;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{id}")
    public SupplierDto updateProcess(@PathVariable Integer id, @Valid @RequestBody SupplierDto supplierDto) {
        return supplierService.updateSupplier(1, supplierDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
