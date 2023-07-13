package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActionController.class)
public class ActionControllerTest extends BaseControllerTest {

    @Autowired
    private ActionController actionController;

    @MockBean
    private ActionService actionService;

    private ActionDto testactionDto;

    @Override
    protected Object getController() {
        return actionController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
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
    public void shouldGetActionById() throws Exception {
        when(actionService.getActionById(any(Integer.class))).thenReturn(testactionDto);

        mockMvc.perform(get("/actions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("test"));
    }

//    @Test
//    @WithMockUser(username = "bill", roles = "ADMIN")
//    public void shouldGetAllActions() throws Exception {
//        when(actionService.getAllActions()).thenReturn(Arrays.asList(testactionDto));
//
//        mockMvc.perform(get("/actions"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].description").value("test"));
//    }

//    @Test
//    public void shouldReturnFilteredActions() throws Exception{
//        when(actionService.getFilteredActions(anyString(), anyString(), anyBoolean(), anyList(), anyList(), anyString(), anyString(), anyList()))
//                .thenReturn(Arrays.asList(testactionDto));
//
//        mockMvc.perform(get("/actions/filtered")
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

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
