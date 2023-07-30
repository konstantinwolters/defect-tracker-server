package com.example.defecttrackerserver.core.lot.supplier;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Save Supplier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping
    public SupplierDto saveSupplier(@Valid @RequestBody SupplierDto supplierDto) {
        return supplierService.saveSupplier(supplierDto);
    }

    @Operation(
            summary = "Get Supplier by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Supplier not found"),
            }
    )
    @GetMapping("/{id}")
    public SupplierDto getSupplier(@PathVariable Integer id) {
        return supplierService.getSupplierById(id);
    }

    @Operation(
            summary = "Get all Suppliers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Suppliers found"),
            }
    )
    @GetMapping
    public List<SupplierDto> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @Operation(
            summary = "Update Supplier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Supplier not found"),
            }
    )
    @PutMapping("/{id}")
    public SupplierDto updateSupplier(@PathVariable Integer id, @Valid @RequestBody SupplierDto supplierDto) {
        return supplierService.updateSupplier(id, supplierDto);
    }

    @Operation(
            summary = "Delete Supplier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Supplier successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Supplier not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
