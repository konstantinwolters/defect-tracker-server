package com.example.defecttrackerserver.core.defect.causationCategory;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CausationCategoryIntegrationTest extends BaseIntegrationTest {
    String URL = "/causationcategories";

    Role roleQA;
    Role roleADMIN;
    User user;
    Location location;

    @BeforeEach
    void setUp() {
        commonSetup();
    }

    @Test
    @Transactional
    void shouldSaveCausationCategory() throws Exception {

        CausationCategoryDto causationCategoryDto = new CausationCategoryDto();
        causationCategoryDto.setName("testCausationCategory");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(causationCategoryDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        CausationCategory savedCausationCategory = causationCategoryRepository.findAll().get(0);

        assertEquals(causationCategoryDto.getName(), savedCausationCategory.getName());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        CausationCategoryDto causationCategoryDto = new CausationCategoryDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(causationCategoryDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        CausationCategoryDto causationCategoryDto = new CausationCategoryDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(causationCategoryDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetCausationCategoryById() throws Exception{
        CausationCategory causationCategory = setUpCausationCategory("testCausationCategory");

        CausationCategoryDto causationCategoryDto = causationCategoryMapper.mapToDto(causationCategory);

        mockMvc.perform(get(URL + "/" + causationCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(causationCategoryDto)));
    }

    @Test
    @Transactional
    void shouldGetAllCausationCategories() throws Exception{
        setUpCausationCategory("testCausationCategory1");
        setUpCausationCategory("testCausationCategory2");

        MvcResult result = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<?> list = mapper.readValue(content, List.class);

        assertEquals(2, list.size());
    }

    @Test
    @Transactional
    void shouldUpdateCausationCategory() throws Exception{
        CausationCategory causationCategory = setUpCausationCategory("testCausationCategory");

        CausationCategoryDto causationCategoryDto = causationCategoryMapper.mapToDto(causationCategory);
        causationCategoryDto.setName("UpdatedTestCausationCategory");

        mockMvc.perform(put(URL + "/" + causationCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(causationCategoryDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        Optional<CausationCategory> savedCausationCategory =
                causationCategoryRepository.findById(causationCategory.getId());

        assertTrue(savedCausationCategory.isPresent());
        assertEquals(causationCategoryDto.getName(), savedCausationCategory.get().getName());
    }

    @Test
    @Transactional
    void shouldDeleteCausationCategoryById() throws Exception{

        CausationCategory causationCategory = setUpCausationCategory("testCausationCategory");

        mockMvc.perform(delete(URL + "/" + causationCategory.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
