package com.example.defecttrackerserver.core.location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    public LocationDto saveLocation(@RequestBody LocationDto locationDto) {
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

    @PutMapping
    public LocationDto updateLocation(@RequestBody LocationDto locationDto) {
        return locationService.updateLocation(locationDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
    }
}
