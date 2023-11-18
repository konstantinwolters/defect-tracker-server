package com.example.defecttrackerserver.core.defect.process;

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

public class ProcessIntegrationTest extends BaseIntegrationTest {
    String URL = "/processes";

    @BeforeEach
    void setUp() {
        commonSetup();
    }

    @Test
    @Transactional
    void shouldSaveProcess() throws Exception {
        ProcessDto processDto = new ProcessDto();
        processDto.setName("testProcess");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(processDto)))
                .andExpect(status().isOk());

        Process savedProcess = processRepository.findAll().get(0);

        assertEquals(processDto.getName(), savedProcess.getName());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        ProcessDto processDto = new ProcessDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(processDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        ProcessDto processDto = new ProcessDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(processDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetProcessId() throws Exception{
        Process process = setUpProcess("testProcess");

        ProcessDto processDto = processMapper.mapToDto(process);

        mockMvc.perform(get(URL + "/" + process.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(processDto)));
    }

    @Test
    @Transactional
    void shouldGetAllProcesses() throws Exception{
        setUpProcess("testProcess1");
        setUpProcess("testProcess2");

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
    void shouldUpdateProcess() throws Exception{
        Process process = setUpProcess("testProcess");

        ProcessDto processDto = processMapper.mapToDto(process);
        processDto.setName("UpdatedTestProcess");

        mockMvc.perform(put(URL + "/" + process.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(processDto)))
                .andExpect(status().isOk());

        Optional<Process> savedProcess =
                processRepository.findById(process.getId());

        assertTrue(savedProcess.isPresent());
        assertEquals(processDto.getName(), savedProcess.get().getName());
    }

    @Test
    @Transactional
    void shouldDeleteProcessById() throws Exception{
        Process process = setUpProcess("testProcess");

        mockMvc.perform(delete(URL + "/" + process.getId()))
                .andExpect(status().isNoContent());
    }
}
