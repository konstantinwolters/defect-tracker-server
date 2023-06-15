package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActionController.class)
@Import(SecurityConfig.class)
public class ActionControllerTest {

    @MockBean
    private ActionService actionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private ActionDto testactionDto;

    @BeforeEach
    public void setUp() {
        testactionDto = new ActionDto();
        testactionDto.setDescription("test");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveAction() throws Exception {
        when(actionService.saveAction(any(ActionDto.class))).thenReturn(testactionDto);

        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testactionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetAction() throws Exception {
        when(actionService.getActionById(any(Integer.class))).thenReturn(testactionDto);

        mockMvc.perform(get("/actions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetAllActions() throws Exception {
        when(actionService.getAllActions()).thenReturn(Arrays.asList(testactionDto));

        mockMvc.perform(get("/actions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].description").value("test"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetAllActionsByUserCreatedId() throws Exception{
        when(actionService.getAllActionsByUserCreatedId(any(Integer.class))).thenReturn(Arrays.asList(testactionDto));

        mockMvc.perform(get("/actions/created-by/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].description").value("test"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateAction() throws Exception {
        when(actionService.updateAction(any(ActionDto.class))).thenReturn(testactionDto);
        mockMvc.perform(put("/actions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testactionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteAction() throws Exception {
        doNothing().when(actionService).deleteAction(any(Integer.class));
        mockMvc.perform(delete("/actions/1"))
                .andExpect(status().isOk());
    }
}
