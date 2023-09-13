package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DefectImageController.class)
public class DefectImageControllerTest extends BaseControllerTest {

    @Autowired
    private DefectImageController defectImageController;

    @MockBean
    private DefectImageServiceImpl defectImageService;

    private DefectImageDto testDefectImageDto;

    @Override
    protected Object getController() {
        return defectImageController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testDefectImageDto = TestHelper.setUpDefectImageDto();
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldAddImageToDefect() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpg", "some-image-data".getBytes());

        when(defectImageService.saveDefectImageToDefect(any(Integer.class), any(MultipartFile.class))).thenReturn(testDefectImageDto);

        mockMvc.perform(multipart("/defects/1/images")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
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
    public void shouldDeleteDefectImage() throws Exception {
        doNothing().when(defectImageService).deleteDefectImage(any(Integer.class), any(Integer.class));
        mockMvc.perform(delete("/defects/1/images/1"))
                .andExpect(status().isNoContent());
    }
}
