package com.example.defecttrackerserver.core.location;

import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public LocationDto mapToDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setName(location.getName());
        return locationDto;
    }
}
