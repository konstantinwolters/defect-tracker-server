package com.example.defecttrackerserver.core.lot.material;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing {@link Material}.
 * Provides endpoints for creating, updating, deleting, and retrieving materials.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/materials")
@Tag(name = "Materials")
public class MaterialController {
    private final MaterialService materialService;

    @Operation(
            summary = "Save Material",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Material saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping
    public MaterialDto saveMaterial(@Valid @RequestBody MaterialDto materialDto) {
        return materialService.saveMaterial(materialDto);
    }

    @Operation(
            summary = "Get Material by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Material found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Material not found"),
            }
    )
    @GetMapping("/{id}")
    public MaterialDto getMaterial(@PathVariable Integer id) {
        return materialService.getMaterialById(id);
    }

    @Operation(
            summary = "Get all Materials",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Material found"),
            }
    )
    @GetMapping
    public List<MaterialDto> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @Operation(
            summary = "Update Material",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Material updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Material not found"),
            }
    )
    @PutMapping("/{id}")
    public MaterialDto updateMaterial(@PathVariable Integer id, @Valid @RequestBody MaterialDto materialDto) {
        return materialService.updateMaterial(id, materialDto);
    }

    @Operation(
            summary = "Delete Material",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Material successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Material not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Integer id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
