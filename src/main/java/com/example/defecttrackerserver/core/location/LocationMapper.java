package com.example.defecttrackerserver.core.location;

import org.springframework.stereotype.Component;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
public class LocationMapper {
    public LocationDto mapToDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setCustomId(location.getCustomId());
        locationDto.setName(location.getName());
        return locationDto;
    }
}
