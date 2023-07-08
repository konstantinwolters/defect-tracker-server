package com.example.defecttrackerserver.core.defect.defectType;

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

@WebMvcTest(DefectTypeController.class)
public class DefectTypeControllerTest extends BaseControllerTest {

    @Autowired
    private DefectTypeController defectTypeController;

    @MockBean
    private DefectTypeServiceImpl defectTypeService;
    private DefectTypeDto testDefectTypeDto;

    @Override
    protected Object getController() {
        return defectTypeController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testDefectTypeDto = new DefectTypeDto();
        testDefectTypeDto.setName("testName");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveDefectType() throws Exception {
        when(defectTypeService.saveDefectType(any(DefectTypeDto.class))).thenReturn(testDefectTypeDto);

        mockMvc.perform(post("/defecttypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDefectTypeDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetDefectTypeById() throws Exception {
        when(defectTypeService.getDefectTypeById(any(Integer.class))).thenReturn(testDefectTypeDto);

        mockMvc.perform(get("/defecttypes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldGetAllDefectTypes() throws Exception {
        when(defectTypeService.getAllDefectTypes()).thenReturn(Arrays.asList(testDefectTypeDto));

        mockMvc.perform(get("/defecttypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateDefectType() throws Exception {
        when(defectTypeService.updateDefectType(any(DefectTypeDto.class))).thenReturn(testDefectTypeDto);
        mockMvc.perform(put("/defecttypes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDefectTypeDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteDefectType() throws Exception {
        doNothing().when(defectTypeService).deleteDefectType(any(Integer.class));
        mockMvc.perform(delete("/defecttypes/1"))
                .andExpect(status().isOk());
    }
}
