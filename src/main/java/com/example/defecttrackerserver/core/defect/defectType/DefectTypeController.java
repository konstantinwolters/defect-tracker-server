package com.example.defecttrackerserver.core.defect.defectType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defecttypes")
@Tag(name = "DefectTypes")
public class DefectTypeController {
    private final DefectTypeService defectTypeService;

    @Operation(
            summary = "Add a DefectType",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectType saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping
    public DefectTypeDto saveDefectType(@Valid @RequestBody DefectTypeDto defectTypeDto) {
        return defectTypeService.saveDefectType(defectTypeDto);
    }

    @Operation(
            summary = "Get DefectType by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectType found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectType not found"),
            }
    )
    @GetMapping("/{id}")
    public DefectTypeDto getDefectType(@PathVariable Integer id) {
        return defectTypeService.getDefectTypeById(id);
    }

    @Operation(
            summary = "Get all DefectType",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectTypes found"),
            }
    )
    @GetMapping
    public List<DefectTypeDto> getAllDefectTypes() {
        return defectTypeService.getAllDefectTypes();
    }

    @Operation(
            summary = "Update DefectType",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectType updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectType not found"),
            }
    )
    @PutMapping("/{id}")
    public DefectTypeDto updateDefectType(@PathVariable Integer id, @Valid @RequestBody DefectTypeDto defectTypeDto) {
        return defectTypeService.updateDefectType(1, defectTypeDto);
    }

    @Operation(
            summary = "Delete DefectType",
            responses = {
                    @ApiResponse(responseCode = "204", description = "DefectType deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectType not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefectType(@PathVariable Integer id) {
        defectTypeService.deleteDefectType(id);
        return ResponseEntity.noContent().build();
    }
}
