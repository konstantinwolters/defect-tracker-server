package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DefectCommentController.class)
public class DefectCommentControllerTest extends BaseControllerTest {

    @Autowired
    private DefectCommentController defectCommentController;

    @MockBean
    private DefectCommentService defectCommentService;

    private DefectCommentDto testDefectCommentDto;

    @Override
    protected Object getController() {
        return defectCommentController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testDefectCommentDto = TestHelper.setUpDefectCommentDto();
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldAddDefectCommentToDefect() throws Exception {
        when(defectCommentService.addDefectCommentToDefect(any(Integer.class), any(DefectCommentDto.class)))
                .thenReturn(testDefectCommentDto);

        mockMvc.perform(post("/defects/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDefectCommentDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("testContent"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetDefectCommentById() throws Exception {
        when(defectCommentService.getDefectCommentById(any(Integer.class))).thenReturn(testDefectCommentDto);

        mockMvc.perform(get("/defects/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("testContent"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateDefectComment() throws Exception {
        when(defectCommentService.updateDefectComment(any(Integer.class), any(DefectCommentDto.class)))
                .thenReturn(testDefectCommentDto);
        mockMvc.perform(put("/defects/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDefectCommentDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("testContent"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteDefectComment() throws Exception {

        doNothing().when(defectCommentService).deleteDefectComment(any(Integer.class), any(Integer.class));
        mockMvc.perform(delete("/defects/1/comments/1"))
                .andExpect(status().isNoContent());
    }
}
