package com.example.defecttrackerserver.core.location;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository  locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationDto saveLocation(LocationDto locationDto) {
        if(locationDto.getName() == null)
            throw new IllegalArgumentException("Location name must not be null");

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
    public LocationDto updateLocation(LocationDto locationDto) {
        if(locationDto.getId() == null)
            throw new IllegalArgumentException("Location id must not be null");
        if(locationDto.getName() == null)
            throw new IllegalArgumentException("Location name must not be null");

        Location location = locationRepository.findById(locationDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Location not found with id: "
                        + locationDto.getId()));

        location.setName(locationDto.getName());
        Location savedLocation = locationRepository.save(location);

        return locationMapper.mapToDto(savedLocation);
    }

    @Override
    public void deleteLocation(Integer id) {
        locationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Location not found with id: " + id));

        locationRepository.deleteById(id);
    }
}
