package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.core.action.ActionController;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionService;
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

@WebMvcTest(DefectController.class)
@Import(SecurityConfig.class)
public class DefectControllerTest {

    @MockBean
    private DefectService defectService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private DefectDto testDefectDto;

    @BeforeEach
    public void setUp() {
        testDefectDto = new DefectDto();
        testDefectDto.setLocation("Texas");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveAction() throws Exception {
        when(defectService.saveDefect(any(DefectDto.class))).thenReturn(testDefectDto);

        mockMvc.perform(post("/defects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDefectDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("Texas"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetActionById() throws Exception {
        when(defectService.getDefectById(any(Integer.class))).thenReturn(testDefectDto);

        mockMvc.perform(get("/defects/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("Texas"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetAllActions() throws Exception {
        when(defectService.getAllDefects()).thenReturn(Arrays.asList(testDefectDto));

        mockMvc.perform(get("/defects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].location").value("Texas"));
    }


    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateAction() throws Exception {
        when(defectService.updateDefect(any(DefectDto.class))).thenReturn(testDefectDto);
        mockMvc.perform(put("/defects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDefectDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("Texas"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteAction() throws Exception {
        doNothing().when(defectService).deleteDefect(any(Integer.class));
        mockMvc.perform(delete("/defects/1"))
                .andExpect(status().isOk());
    }
}
