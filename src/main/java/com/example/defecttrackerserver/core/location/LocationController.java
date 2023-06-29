package com.example.defecttrackerserver.core.location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    public LocationDto saveProcess(@RequestBody LocationDto locationDto) {
        return locationService.saveLocation(locationDto);
    }

    @GetMapping("/{id}")
    public LocationDto getProcess(@PathVariable Integer id) {
        return locationService.getLocationById(id);
    }

    @GetMapping
    public List<LocationDto> getAllDefectTypes() {
        return locationService.getAllLocations();
    }

    @PutMapping
    public LocationDto updateDefectType(@RequestBody LocationDto locationDto) {
        return locationService.updateLocation(locationDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDefectType(@PathVariable Integer id) {
        locationService.deleteLocation(id);
    }
}
