package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing {@link Lot}.
 * Provides endpoints for creating, updating, deleting, and retrieving lots.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/lots")
@Tag(name = "Lots")
public class LotController {
    private final LotService lotService;

    @Operation(
            summary = "Save Lot",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping
    public LotDto saveLot(@Valid @RequestBody LotDto lotDto) {
        return lotService.saveLot(lotDto);
    }

    @Operation(
            summary = "Get Lot by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Lot not found"),
            }
    )
    @GetMapping("/{id}")
    public LotDto getLotById(@PathVariable Integer id) {
        return lotService.getLotById(id);
    }

    @Operation(
            summary = "Get all Lots",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot saved successfully"),
            }
    )
    @GetMapping
    public List<LotDto> getAllLots() {
        return lotService.getAllLots();
    }

    @Operation(
            summary = "Update Lot",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Lot not found"),
            }
    )
    @PutMapping("/{id}")
    public LotDto updateLot(@PathVariable Integer id, @Valid @RequestBody LotDto lotDto) {
        return lotService.updateLot(id, lotDto);
    }

    @Operation(
            summary = "Delete Lot",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Lot successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Lot not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLot(@PathVariable Integer id) {
        lotService.deleteLot(id);
        return ResponseEntity.noContent().build();
    }
}
