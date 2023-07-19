package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.core.location.locationException.LocationExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository  locationRepository;
    private final LocationMapper locationMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public LocationDto saveLocation(LocationDto locationDto) {
        if(locationRepository.findByName(locationDto.getName()).isPresent())
            throw new LocationExistsException("Location already exists with name: " + locationDto.getName());

        Location location = new Location();
        location.setName(locationDto.getName());

        Location savedLocation = locationRepository.save(location);

        return locationMapper.mapToDto(savedLocation);
    }

    @Override
    public LocationDto getLocationById(Integer id) {
        return locationRepository.findById(id)
                .map(locationMapper::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
    }

    @Override
    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(locationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public LocationDto updateLocation(Integer locationId, LocationDto locationDto) {
        if(locationDto.getId() == null)
            throw new IllegalArgumentException("Location id must not be null");

        Location location = locationRepository.findById(locationId)
                .orElseThrow(()-> new EntityNotFoundException("Location not found with id: "
                        + locationId));

        Optional<Location> locationExists = locationRepository.findByName(locationDto.getName());
        if(locationExists.isPresent() && !locationExists.get().getId().equals(location.getId()))
            throw new LocationExistsException("Location already exists with name: " + locationDto.getName());

        location.setName(locationDto.getName());

        Location savedLocation = locationRepository.save(location);

        return locationMapper.mapToDto(savedLocation);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteLocation(Integer id) {
        locationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Location not found with id: " + id));

        locationRepository.deleteById(id);
    }
}
