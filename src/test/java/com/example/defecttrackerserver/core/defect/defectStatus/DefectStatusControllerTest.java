package com.example.defecttrackerserver.core.defect.defectStatus;

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

@WebMvcTest(DefectStatusController.class)
public class DefectStatusControllerTest extends BaseControllerTest {

    @Autowired
    private DefectStatusController defectStatusController;

    @MockBean
    private DefectStatusServiceImpl defectStatusService;

    private DefectStatusDto testDefectStatusDto;

    @Override
    protected Object getController() {
        return defectStatusController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
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
    public void shouldUpdateDefectStatus() throws Exception {
        when(defectStatusService.updateDefectStatus(any(Integer.class), any(DefectStatusDto.class))).thenReturn(testDefectStatusDto);
        mockMvc.perform(put("/defectstatus/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDefectStatusDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteDefectStatus() throws Exception {
        doNothing().when(defectStatusService).deleteDefectStatus(any(Integer.class));
        mockMvc.perform(delete("/defectstatus/1"))
                .andExpect(status().isNoContent());
    }
}
