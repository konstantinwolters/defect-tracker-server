package com.example.defecttrackerserver.core.defect.process;

import com.example.defecttrackerserver.BaseControllerTest;
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

@WebMvcTest(ProcessController.class)
public class ProcessControllerTest extends BaseControllerTest {

    @Autowired
    private ProcessController processController;

    @MockBean
    private ProcessServiceImpl processService;

    private ProcessDto testProcessDto;

    @Override
    protected Object getController() {
        return processController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testProcessDto = new ProcessDto();
        testProcessDto.setName("testName");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveProcess() throws Exception {
        when(processService.saveProcess(any(ProcessDto.class))).thenReturn(testProcessDto);

        mockMvc.perform(post("/processes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProcessDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetProcessById() throws Exception {
        when(processService.getProcessById(any(Integer.class))).thenReturn(testProcessDto);

        mockMvc.perform(get("/processes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldGetAllProcesses() throws Exception {
        when(processService.getAllProcesses()).thenReturn(Arrays.asList(testProcessDto));

        mockMvc.perform(get("/processes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateProcess() throws Exception {
        when(processService.updateProcess(any(Integer.class), any(ProcessDto.class))).thenReturn(testProcessDto);
        mockMvc.perform(put("/processes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProcessDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteProcess() throws Exception {
        doNothing().when(processService).deleteProcess(any(Integer.class));
        mockMvc.perform(delete("/processes/1"))
                .andExpect(status().isOk());
    }
}
