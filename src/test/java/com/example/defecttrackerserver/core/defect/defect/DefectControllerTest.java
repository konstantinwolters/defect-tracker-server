package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.response.PaginatedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DefectController.class)
public class DefectControllerTest extends BaseControllerTest {

    @Autowired
    private DefectController defectController;

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
        testDefectDto = new DefectDto();
        testDefectDto.setLocation("Texas");
        testDefectDto.setDescription("Test");
        testDefectDto.setDefectType("TestType");
        testDefectDto.setLot("TestLot");
        testDefectDto.setProcess("TestProcess");
        testDefectDto.setCreatedBy(new UserDto());

    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveDefect() throws Exception {
        when(defectService.saveDefect(any(DefectDto.class))).thenReturn(testDefectDto);

        mockMvc.perform(post("/defects")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDefectDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("Texas"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetDefectById() throws Exception {
        when(defectService.getDefectById(any(Integer.class))).thenReturn(testDefectDto);

        mockMvc.perform(get("/defects/1").with(csrf()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("Texas"));
    }

    @Test
    public void shouldReturnFilteredDefects() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        PaginatedResponse<DefectDto> response = new PaginatedResponse<>(List.of(testDefectDto), 1,
                1, 0, new DefectFilterValues());

        when(defectService.getDefects(anyList(),anyList(), any(), any(), anyList(), anyList(), anyList(),
                anyList(), any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/defects")
                        .param("lotIds", "1", "2")
                        .param("defectStatusIds", "3", "4")
                        .param("createdAtStart", "2023-01-01")
                        .param("createdAtEnd", "2023-12-31")
                        .param("locationIds", "5", "6")
                        .param("processIds", "7", "8")
                        .param("defectTypeIds", "9", "10")
                        .param("createdByIds", "11", "12")
                        .param("page", "0")
                        .param("size", "10")
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
        when(defectService.updateDefect(any(Integer.class), any(DefectDto.class))).thenReturn(testDefectDto);
        mockMvc.perform(put("/defects/1")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDefectDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value("Texas"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteDefect() throws Exception {
        doNothing().when(defectService).deleteDefect(any(Integer.class));
        mockMvc.perform(delete("/defects/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
