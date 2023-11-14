package com.example.defecttrackerserver.core.defect.causationCategory;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.TestHelper;
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

@WebMvcTest(CausationCategoryController.class)
public class CausationCategoryControllerTest extends BaseControllerTest {

    @Autowired
    private CausationCategoryController causationCategoryController;

    @MockBean
    private CausationCategoryServiceImpl causationCategoryService;

    private CausationCategoryDto testCausationCategoryDto;

    @Override
    protected Object getController() {
        return causationCategoryController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testCausationCategoryDto = TestHelper.setUpCausationCategoryDto();
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveCausationCategory() throws Exception {
        when(causationCategoryService.saveCausationCategory(any(CausationCategoryDto.class)))
                .thenReturn(testCausationCategoryDto);

        mockMvc.perform(post("/causationcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCausationCategoryDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testCausationCategory"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldCausationCategoryById() throws Exception {
        when(causationCategoryService.getCausationCategoryById(any(Integer.class)))
                .thenReturn(testCausationCategoryDto);

        mockMvc.perform(get("/causationcategories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testCausationCategory"));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldGetAllCausationCategories() throws Exception {
        when(causationCategoryService.getAllCausationCategories())
                .thenReturn(Arrays.asList(testCausationCategoryDto));

        mockMvc.perform(get("/causationcategories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("testCausationCategory"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateCausationCategory() throws Exception {
        when(causationCategoryService.updateCausationCategory(any(Integer.class), any(CausationCategoryDto.class)))
                .thenReturn(testCausationCategoryDto);

        mockMvc.perform(put("/causationcategories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCausationCategoryDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testCausationCategory"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteCausationCategory() throws Exception {
        doNothing().when(causationCategoryService).deleteCausationCategory(any(Integer.class));

        mockMvc.perform(delete("/causationcategories/1"))
                .andExpect(status().isNoContent());
    }
}
