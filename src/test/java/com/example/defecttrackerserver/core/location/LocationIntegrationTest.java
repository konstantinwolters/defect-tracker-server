package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LocationIntegrationTest extends BaseIntegrationTest {
    String URL = "/locations";

    @BeforeEach
    void setUp() {
        commonSetup();
    }

    @Test
    @Transactional
    void shouldSaveLocation() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setName("testLocation2");
        locationDto.setCustomId("testCustomId");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isOk());

        Location savedLocation = locationRepository.findAll().get(1); // get(1), because BaseIntegrationTest already inserts a location

        assertEquals(locationDto.getName(), savedLocation.getName());
        assertEquals(locationDto.getCustomId(), savedLocation.getCustomId());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        LocationDto locationDto = new LocationDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        LocationDto locationDto = new LocationDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetLocationById() throws Exception{
        Location location = setUpLocation("testLocation2");

        LocationDto locationDto = locationMapper.mapToDto(location);

        mockMvc.perform(get(URL + "/" + location.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(locationDto)));
    }

    @Test
    @Transactional
    void shouldGetAllLocations() throws Exception{
        setUpLocation("testLocation2");

        MvcResult result = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<?> list = mapper.readValue(content, List.class);

        assertEquals(2, list.size());
    }

    @Test
    @Transactional
    void shouldUpdateLocation() throws Exception{
        Location location = setUpLocation("testLocation2");

        LocationDto locationDto = locationMapper.mapToDto(location);
        locationDto.setName("UpdatedTestLocation");

        mockMvc.perform(put(URL + "/" + location.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isOk());

        Optional<Location> savedLocation =
                locationRepository.findById(location.getId());

        assertTrue(savedLocation.isPresent());
        assertEquals(locationDto.getName(), savedLocation.get().getName());
    }

    @Test
    @Transactional
    void shouldDeleteLocationById() throws Exception{
        Location location = setUpLocation("testLocation2");

        mockMvc.perform(delete(URL + "/" + location.getId()))
                .andExpect(status().isNoContent());
    }
}
