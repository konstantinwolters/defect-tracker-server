package com.example.defecttrackerserver.core.defect.defectStatus;

import com.example.defecttrackerserver.BaseIntegrationTest;
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

public class DefectStatusIntegrationTest extends BaseIntegrationTest {
    String URL = "/defectstatus";

    @BeforeEach
    void setUp() {
        commonSetup();
    }

    @Test
    @Transactional
    void shouldSaveDefectStatus() throws Exception {

        DefectStatusDto defectStatusDto = new DefectStatusDto();
        defectStatusDto.setName("testDefectStatus");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectStatusDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        DefectStatus savedDefectStatus = defectStatusRepository.findAll().get(0);

        assertEquals(defectStatusDto.getName(), savedDefectStatus.getName());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        DefectStatusDto defectStatusDto = new DefectStatusDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectStatusDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        DefectStatusDto defectStatusDto = new DefectStatusDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectStatusDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetDefectStatusById() throws Exception{
        DefectStatus defectStatus = setUpDefectStatus("testDefectStatus");

        DefectStatusDto defectStatusDto = defectStatusMapper.mapToDto(defectStatus);

        mockMvc.perform(get(URL + "/" + defectStatus.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(defectStatusDto)));
    }

    @Test
    @Transactional
    void shouldGetAllDefectStatuses() throws Exception{
        setUpDefectStatus("testDefectStatus1");
        setUpDefectStatus("testDefectStatus2");

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
    void shouldUpdateDefectStatus() throws Exception{
        DefectStatus defectStatus = setUpDefectStatus("testDefectStatus");

        DefectStatusDto defectStatusDto = defectStatusMapper.mapToDto(defectStatus);
        defectStatusDto.setName("UpdatedTestDefectStatus");

        mockMvc.perform(put(URL + "/" + defectStatus.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectStatusDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        Optional<DefectStatus> savedDefectStatus =
                defectStatusRepository.findById(defectStatus.getId());

        assertTrue(savedDefectStatus.isPresent());
        assertEquals(defectStatusDto.getName(), savedDefectStatus.get().getName());
    }

    @Test
    @Transactional
    void shouldDeleteDefectStatusById() throws Exception{
        DefectStatus defectStatus = setUpDefectStatus("testDefectStatus");

        mockMvc.perform(delete(URL + "/" + defectStatus.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
