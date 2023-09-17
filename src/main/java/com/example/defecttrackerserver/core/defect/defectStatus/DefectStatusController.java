package com.example.defecttrackerserver.core.defect.defectStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing {@link DefectStatus}.
 * Provides endpoints for creating, updating, deleting, and retrieving defect statuses.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/defectstatus")
@Tag(name = "DefectStatus")
public class DefectStatusController {
    private final DefectStatusService defectStatusService;

    @Operation(
            summary = "Save DefectStatus",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectStatus saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping
    public DefectStatusDto saveDefectStatus(@Valid @RequestBody DefectStatusDto defectStatusDto) {
        return defectStatusService.saveDefectStatus(defectStatusDto);
    }

    @Operation(
            summary = "Get DefectStatus by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectStatus found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectStatus not found"),
            }
    )
    @GetMapping("/{id}")
    public DefectStatusDto getDefectStatus(@PathVariable Integer id) {
        return defectStatusService.getDefectStatusById(id);
    }

    @Operation(
            summary = "Get all DefectStatus",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectStatus found"),
            }
    )
    @GetMapping
    public List<DefectStatusDto> getAllDefectStatus() {
        return defectStatusService.getAllDefectStatus();
    }

    @Operation(
            summary = "Update DefectStatus",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectStatus updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectStatus not found"),
            }
    )
    @PutMapping("/{id}")
    public DefectStatusDto updateDefectStatus(@PathVariable Integer id, @Valid @RequestBody DefectStatusDto defectStatusDto) {
        return defectStatusService.updateDefectStatus(id, defectStatusDto);
    }

    @Operation(
            summary = "Delete DefectStatus",
            responses = {
                    @ApiResponse(responseCode = "204", description = "DefectStatus successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectStatus not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefectStatus(@PathVariable Integer id) {
        defectStatusService.deleteDefectStatus(id);
        return ResponseEntity.noContent().build();
    }
}
