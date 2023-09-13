package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationMapperTest {

    @InjectMocks
    private LocationMapper locationMapper;

    private Location location;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        location = TestHelper.setUpLocation();
    }

    @Test
    void shouldReturnMappedLocationDto() {

        LocationDto mappedLocation = locationMapper.mapToDto(location);

        assertEquals(location.getId(), mappedLocation.getId());
        assertEquals(location.getName(), mappedLocation.getName());
    }
}

