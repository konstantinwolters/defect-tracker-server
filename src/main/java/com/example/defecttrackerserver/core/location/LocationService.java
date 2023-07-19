package com.example.defecttrackerserver.core.location;

import java.util.List;

public interface LocationService {
    LocationDto saveLocation(LocationDto locationDto);
    LocationDto getLocationById(Integer id);
    List<LocationDto> getAllLocations();
    LocationDto updateLocation(Integer locationId, LocationDto locationDto);
    void deleteLocation(Integer id);
}
