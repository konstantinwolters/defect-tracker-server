package com.example.defecttrackerserver.core.defect.causationCategory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller managing {@link CausationCategory}.
 * Provides endpoints for creating, updating, deleting, and retrieving causation categories.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/causationcategories")
@Tag(name = "CausationCategory")
public class CausationCategoryController {
    private final CausationCategoryService causationCategoryService;

    @Operation(
            summary = "Save CausationCategory",
            responses = {
                    @ApiResponse(responseCode = "200", description = "CausationCategory saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping
    public CausationCategoryDto saveCausationCategory(@Valid @RequestBody CausationCategoryDto causationCategoryDto) {
        return causationCategoryService.saveCausationCategory(causationCategoryDto);
    }

    @Operation(
            summary = "Get CausationCategory by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "CausationCategory found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "CausationCategory not found"),
            }
    )
    @GetMapping("/{id}")
    public CausationCategoryDto getCausationCategory(@PathVariable Integer id) {
        return causationCategoryService.getCausationCategoryById(id);
    }

    @Operation(
            summary = "Get all CausationCategories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "CausationCategories found"),
            }
    )
    @GetMapping
    public List<CausationCategoryDto> getAllCausationCategories() {
        return causationCategoryService.getAllCausationCategories();
    }

    @Operation(
            summary = "Update CausationCategory",
            responses = {
                    @ApiResponse(responseCode = "200", description = "CausationCategory updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "CausationCategory not found"),
            }
    )
    @PutMapping("/{id}")
    public CausationCategoryDto updateCausationCategory(@PathVariable Integer id, @Valid @RequestBody CausationCategoryDto causationCategoryDto) {
        return causationCategoryService.updateCausationCategory(id, causationCategoryDto);
    }

    @Operation(
            summary = "Delete CausationCategory",
            responses = {
                    @ApiResponse(responseCode = "204", description = "CausationCategory deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "CausationCategory not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCausationCategory(@PathVariable Integer id) {
        causationCategoryService.deleteCausationCategory(id);
        return ResponseEntity.noContent().build();
    }
}
