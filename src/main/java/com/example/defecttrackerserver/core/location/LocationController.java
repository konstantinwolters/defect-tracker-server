package com.example.defecttrackerserver.core.location;

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
@RequestMapping("/locations")
@Tag(name = "Locations")
public class LocationController {
    private final LocationService locationService;

    @Operation(
            summary = "Save Location",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Location saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping
    public LocationDto saveLocation(@Valid @RequestBody LocationDto locationDto) {
        return locationService.saveLocation(locationDto);
    }

    @Operation(
            summary = "Get Location by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Location saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Location not found"),
            }
    )
    @GetMapping("/{id}")
    public LocationDto getLocation(@PathVariable Integer id) {
        return locationService.getLocationById(id);
    }

    @Operation(
            summary = "Get all Locations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Location saved successfully"),
            }
    )
    @GetMapping
    public List<LocationDto> getAllLocation() {
        return locationService.getAllLocations();
    }

    @Operation(
            summary = "Update Location",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Location updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Location not found"),
            }
    )
    @PutMapping("/{id}")
    public LocationDto updateLocation(@PathVariable Integer id, @Valid@RequestBody LocationDto locationDto) {
        return locationService.updateLocation(id, locationDto);
    }

    @Operation(
            summary = "Delete Location",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Location successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Location not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
