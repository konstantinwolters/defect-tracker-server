package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.location.locationException.LocationExistsException;
import com.example.defecttrackerserver.core.user.user.UserRepository;
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
    private final UserRepository userRepository;
    private final DefectRepository defectRepository;
    private final LocationMapper locationMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public LocationDto saveLocation(LocationDto locationDto) {
        if(locationRepository.findByName(locationDto.getName()).isPresent())
            throw new LocationExistsException("Location already exists with name: " + locationDto.getName());

        Location location = new Location();
        location.setName(locationDto.getName());
        location.setCustomId(locationDto.getCustomId());

        Location savedLocation = locationRepository.save(location);

        return locationMapper.mapToDto(savedLocation);
    }

    @Override
    public LocationDto getLocationById(Integer id) {
        Location location = findLocationById(id);
        return locationMapper.mapToDto(location);
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
        Location location = findLocationById(locationId);

        Optional<Location> locationExists = locationRepository.findByName(locationDto.getName());
        if(locationExists.isPresent() && !locationExists.get().getId().equals(location.getId()))
            throw new LocationExistsException("Location already exists with name: " + locationDto.getName());

        location.setName(locationDto.getName());
        location.setCustomId(locationDto.getCustomId());

        Location savedLocation = locationRepository.save(location);

        return locationMapper.mapToDto(savedLocation);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteLocation(Integer id) {
       Location location = findLocationById(id);

        if(!defectRepository.findByLocationId(id).isEmpty())
            throw new UnsupportedOperationException("Location cannot be deleted because it is used in Defects");

        if(!userRepository.findByLocationId(id).isEmpty())
            throw new UnsupportedOperationException("Process cannot be deleted because it is used in Users");

        locationRepository.delete(location);
    }

    private Location findLocationById(Integer id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
    }
}
