package com.example.defecttrackerserver.core.lot.material;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materials")
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
    public MaterialDto saveProcess(@Valid @RequestBody MaterialDto materialDto) {
        return materialService.saveMaterial(materialDto);
    }

    @Operation(
            summary = "Get Material by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Material found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Material not found"),
            }
    )
    @GetMapping("/{id}")
    public MaterialDto getProcess(@PathVariable Integer id) {
        return materialService.getMaterialById(id);
    }

    @Operation(
            summary = "Get all Materials",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Material found"),
            }
    )
    @GetMapping
    public List<MaterialDto> getAllProcesses() {
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
    public MaterialDto updateProcess(@PathVariable Integer id, @Valid @RequestBody MaterialDto materialDto) {
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
    public ResponseEntity<Void> deleteProcess(@PathVariable Integer id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
