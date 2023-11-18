package com.example.defecttrackerserver.core.defect.defectType;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DefectTypeIntegrationTest extends BaseIntegrationTest {
    String URL = "/defecttypes";

    @BeforeEach
    void setUp() {
        commonSetup();
    }

    @Test
    @Transactional
    void shouldSaveDefectType() throws Exception {

        DefectTypeDto defectTypeDto = new DefectTypeDto();
        defectTypeDto.setName("testDefectType");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectTypeDto)))
                .andExpect(status().isOk());

        DefectType savedDefectType = defectTypeRepository.findAll().get(0);

        assertEquals(defectTypeDto.getName(), savedDefectType.getName());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        DefectTypeDto defectTypeDto = new DefectTypeDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectTypeDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        DefectTypeDto defectTypeDto = new DefectTypeDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectTypeDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetDefectTypeById() throws Exception{
        DefectType defectType = setUpDefectType("testDefectType");

        DefectTypeDto defectTypeDto = defectTypeMapper.mapToDto(defectType);

        mockMvc.perform(get(URL + "/" + defectType.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(defectTypeDto)));
    }

    @Test
    @Transactional
    void shouldGetAllDefectTypes() throws Exception{
        setUpDefectType("testDefectType1");
        setUpDefectType("testDefectType2");

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
    void shouldUpdateDefectType() throws Exception{
        DefectType defectType = setUpDefectType("testDefectType");

        DefectTypeDto defectTypeDto = defectTypeMapper.mapToDto(defectType);
        defectTypeDto.setName("UpdatedTestDefectStatus");

        mockMvc.perform(put(URL + "/" + defectType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectTypeDto)))
                .andExpect(status().isOk());

        Optional<DefectType> savedDefectType =
                defectTypeRepository.findById(defectType.getId());

        assertTrue(savedDefectType.isPresent());
        assertEquals(defectTypeDto.getName(), savedDefectType.get().getName());
    }

    @Test
    @Transactional
    void shouldDeleteDefectTypeById() throws Exception{
        DefectType defectType = setUpDefectType("testDefectType");

        mockMvc.perform(delete(URL + "/" + defectType.getId()))
                .andExpect(status().isNoContent());
    }
}
