package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DefectController.class)
@RequiredArgsConstructor
public class DefectControllerTest extends BaseControllerTest {

    @Autowired
    private DefectController defectController;

    @MockBean
    private Utils utils;

    @MockBean
    private DefectService defectService;

    private DefectDto testDefectDto;

    @Override
    protected Object getController() {
        return defectController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testDefectDto = TestHelper.setUpDefectDto();
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveDefect() throws Exception {
        when(defectService.saveDefect(any(DefectDto.class), any(MultipartFile[].class))).thenReturn(testDefectDto);

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.jpg", "image/jpg", "some-image-data".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.jpg", "image/jpg", "some-other-image-data".getBytes());

        String defectDtoAsString = objectMapper.writeValueAsString(testDefectDto);

        MockPart defectPart = new MockPart("defect", defectDtoAsString.getBytes());
        defectPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(multipart("/defects")
                        .file(image1)
                        .file(image2)
                        .part(defectPart)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("testLocation"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetDefectById() throws Exception {
        when(defectService.getDefectById(any(Integer.class))).thenReturn(testDefectDto);

        mockMvc.perform(get("/defects/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("testLocation"));
    }

    @Test
    public void shouldReturnFilteredDefects() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        PaginatedResponse<DefectDto> response = new PaginatedResponse<>(List.of(testDefectDto), 1,
                1, 0, new DefectFilterValues());

        when(defectService.getDefects(anyString(), anyString(),anyString(), anyString(), anyString(), anyString(), any(), any(),
                any(), any(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString()))
                .thenReturn(response);

        when(utils.convertStringToListOfInteger(anyString())).thenReturn(List.of(1,2));

        mockMvc.perform(get("/defects")
                        .param("search", "Test")
                        .param("lotIds", "1", "2")
                        .param("materialIds", "1", "2")
                        .param("supplierIds", "1", "2")
                        .param("defectStatusIds", "3", "4")
                        .param("causationCategoryIds", "3", "4")
                        .param("createdAtStart", "2023-01-01")
                        .param("createdAtEnd", "2023-12-31")
                        .param("changedAtStart", "2023-01-31")
                        .param("changedAtEnd", "2023-12-31")
                        .param("locationIds", "5", "6")
                        .param("processIds", "7", "8")
                        .param("defectTypeIds", "9", "10")
                        .param("createdByIds", "11", "12")
                        .param("changedByIds", "11", "12")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value(testDefectDto.getDescription()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(response.getTotalElements()))
                .andExpect(jsonPath("$.currentPage").value(response.getCurrentPage()));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateDefect() throws Exception {
        when(defectService.updateDefect(any(Integer.class), any(DefectDto.class), any(MultipartFile[].class))).thenReturn(testDefectDto);

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.jpg", "image/jpg", "some-image-data".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.jpg", "image/jpg", "some-other-image-data".getBytes());

        String defectDtoAsString = objectMapper.writeValueAsString(testDefectDto);

        MockPart defectPart = new MockPart("defect", defectDtoAsString.getBytes());
        defectPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(multipart("/defects/1")
                        .file(image1)
                        .file(image2)
                        .part(defectPart)
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("testLocation"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteDefect() throws Exception {
        doNothing().when(defectService).deleteDefect(any(Integer.class));
        mockMvc.perform(delete("/defects/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
