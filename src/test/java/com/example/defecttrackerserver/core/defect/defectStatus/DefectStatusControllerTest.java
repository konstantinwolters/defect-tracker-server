package com.example.defecttrackerserver.core.defect.defectStatus;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DefectStatusController.class)
@Import(SecurityConfig.class)
public class DefectStatusControllerTest {

    @MockBean
    private DefectStatusServiceImpl defectStatusService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private DefectStatusDto testDefectStatusDto;

    @BeforeEach
    public void setUp() {
        testDefectStatusDto = new DefectStatusDto();
        testDefectStatusDto.setName("testName");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveDefectStatus() throws Exception {
        when(defectStatusService.saveDefectStatus(any(DefectStatusDto.class))).thenReturn(testDefectStatusDto);

        mockMvc.perform(post("/defectstatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDefectStatusDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetDefectStatusById() throws Exception {
        when(defectStatusService.getDefectStatusById(any(Integer.class))).thenReturn(testDefectStatusDto);

        mockMvc.perform(get("/defectstatus/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldGetAllDefectStatuses() throws Exception {
        when(defectStatusService.getAllDefectStatus()).thenReturn(Arrays.asList(testDefectStatusDto));

        mockMvc.perform(get("/defectstatus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateDefectComment() throws Exception {
        when(defectStatusService.updateDefectStatus(any(DefectStatusDto.class))).thenReturn(testDefectStatusDto);
        mockMvc.perform(put("/defectstatus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDefectStatusDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteDefectComment() throws Exception {
        doNothing().when(defectStatusService).deleteDefectStatus(any(Integer.class));
        mockMvc.perform(delete("/defectstatus/1"))
                .andExpect(status().isOk());
    }
}
