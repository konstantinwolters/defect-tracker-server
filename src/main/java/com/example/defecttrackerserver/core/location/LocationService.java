package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.core.defect.process.ProcessDto;

import java.util.List;

public interface LocationService {
    LocationDto saveLocation(LocationDto locationDto);
    LocationDto getLocationById(Integer id);
    List<LocationDto> getAllLocations();
    LocationDto updateLocation(LocationDto locationDto);
    void deleteLocation(Integer id);
}
