package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
public class LocationControllerTest extends BaseControllerTest {

    @Autowired
    private LocationController locationController;

    @MockBean
    private LocationServiceImpl locationService;

    private LocationDto testLocationDto;

    @Override
    protected Object getController() {
        return locationController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testLocationDto = new LocationDto();
        testLocationDto.setName("testName");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveLocation() throws Exception {
        when(locationService.saveLocation(any(LocationDto.class))).thenReturn(testLocationDto);

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetLocationById() throws Exception {
        when(locationService.getLocationById(any(Integer.class))).thenReturn(testLocationDto);

        mockMvc.perform(get("/locations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldGetAllLocations() throws Exception {
        when(locationService.getAllLocations()).thenReturn(Arrays.asList(testLocationDto));

        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateLocation() throws Exception {
        when(locationService.updateLocation(any(Integer.class), any(LocationDto.class))).thenReturn(testLocationDto);
        mockMvc.perform(put("/locations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLocationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteLocation() throws Exception {
        doNothing().when(locationService).deleteLocation(any(Integer.class));
        mockMvc.perform(delete("/locations/1"))
                .andExpect(status().isNoContent());
    }
}
