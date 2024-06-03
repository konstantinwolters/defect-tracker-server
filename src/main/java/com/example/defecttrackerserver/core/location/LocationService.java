package com.example.defecttrackerserver.core.location;

import java.util.List;

/**
 * Service interface for managing {@link Location}.
 */
interface LocationService {
    LocationDto saveLocation(LocationDto locationDto);
    LocationDto getLocationById(Integer id);
    List<LocationDto> getAllLocations();
    LocationDto updateLocation(Integer locationId, LocationDto locationDto);
    void deleteLocation(Integer id);
}
