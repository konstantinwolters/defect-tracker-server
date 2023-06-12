package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.core.user.user.UserController;
import com.example.defecttrackerserver.core.user.user.UserService;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private UserDto testuserDto;

    @BeforeEach
    public void setUp() {
        testuserDto = new UserDto();
        testuserDto.setUsername("bill");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveUser() throws Exception {
        when(userService.saveUser(any(UserDto.class))).thenReturn(testuserDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testuserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("bill"));

        verify(userService, times(1)).saveUser(any(UserDto.class));
    }
    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetUserById() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(testuserDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("bill"));

        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testuserDto));

        mockMvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("bill"));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldUpdateUser() throws Exception {
        when(userService.updateUser(any(UserDto.class))).thenReturn(testuserDto);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testuserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("bill"));

        verify(userService, times(1)).updateUser(any(UserDto.class));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(anyInt());
    }
}
