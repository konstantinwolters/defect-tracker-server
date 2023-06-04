package com.example.defecttrackerserver.core.Action;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.core.action.ActionController;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionService;
import com.example.defecttrackerserver.core.user.UserDto;
import com.example.defecttrackerserver.core.user.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ActionController.class)
@Import(SecurityConfig.class)
public class ActionControllerTest {

    @MockBean
    private ActionService  actionService;

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
    public void shouldSaveUser() throws Exception {
        when(actionService.saveAction(any(ActionDto.class))).thenReturn(testactionDto);

        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testactionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("test"));

        verify(actionService, times(1)).saveAction(any(ActionDto.class));
    }
}
