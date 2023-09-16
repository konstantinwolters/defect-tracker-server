package com.example.defecttrackerserver.core.defect.process;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing {@link Process}.
 * Provides endpoints for creating, updating, deleting, and retrieving processes
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/processes")
@Tag(name = "Processes")
public class ProcessController {
    private final ProcessService processService;

    @Operation(
            summary = "Save Process",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Process saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping
    public ProcessDto saveProcess(@Valid @RequestBody ProcessDto processDto) {
        return processService.saveProcess(processDto);
    }

    @Operation(
            summary = "Get Process by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Process found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Process not found"),
            }
    )
    @GetMapping("/{id}")
    public ProcessDto getProcess(@PathVariable Integer id) {
        return processService.getProcessById(id);
    }

    @Operation(
            summary = "Get all Processes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Processes found"),
            }
    )
    @GetMapping
    public List<ProcessDto> getAllProcesses() {
        return processService.getAllProcesses();
    }

    @Operation(
            summary = "Update Process",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Process updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Process not found"),
            }
    )
    @PutMapping("/{id}")
    public ProcessDto updateProcess(@PathVariable Integer id, @Valid @RequestBody ProcessDto processDto) {
        return processService.updateProcess(id, processDto);
    }

    @Operation(
            summary = "Delete Process",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Process successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Process not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Integer id) {
        processService.deleteProcess(id);
        return ResponseEntity.noContent().build();
    }
}
