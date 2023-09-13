package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceImplTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationServiceImpl locationService;

    private LocationDto locationDto;
    private Location location;

    @BeforeEach
    void setUp() {
        locationDto = TestHelper.setUpLocationDto();
        location = TestHelper.setUpLocation();
    }

    @Test
    void shouldSaveLocation() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(locationMapper.mapToDto(any(Location.class))).thenReturn(locationDto);

        LocationDto result = locationService.saveLocation(locationDto);

        assertNotNull(result);
        assertEquals(location.getName(), result.getName());
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    void shouldReturnLocationById() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(locationMapper.mapToDto(location)).thenReturn(locationDto);

        LocationDto result = locationService.getLocationById(1);

        assertNotNull(result);
        assertEquals(location.getId(), result.getId());
        assertEquals(location.getName(), result.getName());
    }

    @Test
    void shouldReturnAllLocations(){
        when(locationRepository.findAll()).thenReturn(Arrays.asList(location));
        when(locationMapper.mapToDto(location)).thenReturn(locationDto);

        List<LocationDto> result = locationService.getAllLocations();

        assertNotNull(result);
        assertEquals(location.getId(), result.get(0).getId());
        assertEquals(location.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateLocation() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(locationMapper.mapToDto(any(Location.class))).thenReturn(locationDto);

        LocationDto result = locationService.updateLocation(1, locationDto);

        assertNotNull(result);
        assertEquals(location.getId(), result.getId());
        assertEquals(location.getName(), result.getName());
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void shouldDeleteLocation() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(defectRepository.findByLocationId(any(Integer.class))).thenReturn(Set.of());
        when(userRepository.findByLocationId(any(Integer.class))).thenReturn(Set.of());

        locationService.deleteLocation(1);

        verify(locationRepository, times(1)).delete(any(Location.class));
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFound(){
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> locationService.deleteLocation(1));
    }
}
