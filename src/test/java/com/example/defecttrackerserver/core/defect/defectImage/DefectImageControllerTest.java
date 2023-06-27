package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusController;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusDto;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusServiceImpl;
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

@WebMvcTest(DefectImageController.class)
@Import(SecurityConfig.class)
public class DefectImageControllerTest {

    @MockBean
    private DefectImageServiceImpl defectImageService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private DefectImageDto testDefectImageDto;

    @BeforeEach
    public void setUp() {
        testDefectImageDto = new DefectImageDto();
        testDefectImageDto.setPath("testPath");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldAddImageToDefect() throws Exception {
        when(defectImageService.saveDefectImageToDefect(any(Integer.class), any(DefectImageDto.class))).thenReturn(testDefectImageDto);

        mockMvc.perform(post("/defects/1/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDefectImageDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("testPath"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetDefectImageById() throws Exception {
        when(defectImageService.getDefectImageById(any(Integer.class))).thenReturn(testDefectImageDto);

        mockMvc.perform(get("/defects/images/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("testPath"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateDefectImage() throws Exception {
        when(defectImageService.updateDefectImage(any(DefectImageDto.class))).thenReturn(testDefectImageDto);
        mockMvc.perform(put("/defects/images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDefectImageDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("testPath"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteDefectImage() throws Exception {
        doNothing().when(defectImageService).deleteDefectImageById(any(Integer.class), any(Integer.class));
        mockMvc.perform(delete("/defects/1/images/1"))
                .andExpect(status().isOk());
    }
}
