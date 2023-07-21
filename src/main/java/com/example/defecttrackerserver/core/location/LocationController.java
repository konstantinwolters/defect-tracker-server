package com.example.defecttrackerserver.core.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    public LocationDto saveLocation(@Valid @RequestBody LocationDto locationDto) {
        return locationService.saveLocation(locationDto);
    }

    @GetMapping("/{id}")
    public LocationDto getLocation(@PathVariable Integer id) {
        return locationService.getLocationById(id);
    }

    @GetMapping
    public List<LocationDto> getAllLocation() {
        return locationService.getAllLocations();
    }

    @PutMapping("/{id}")
    public LocationDto updateLocation(@PathVariable Integer id, @Valid@RequestBody LocationDto locationDto) {
        return locationService.updateLocation(1, locationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
