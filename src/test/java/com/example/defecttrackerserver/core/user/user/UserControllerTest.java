package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.response.PaginatedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest extends BaseControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    private UserDto testuserDto;

    @Override
    protected Object getController() {
        return userController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testuserDto = new UserDto();
        testuserDto.setUsername("bill");
        testuserDto.setMail("test@mail.com");
        testuserDto.setLocation("testLocation");
        testuserDto.setIsActive(true);
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
    public void shouldReturnFilteredUsers() throws Exception {
        String search = "test";
        Boolean isActive = true;
        String locationIds = "1,2";
        String roleIds = "1,2";
        LocalDate createdAtStart = LocalDate.now();
        LocalDate createdAtEnd = LocalDate.now();
        LocalDate changedAtStart = LocalDate.now();
        LocalDate changedAtEnd = LocalDate.now();
        Integer page = 0;
        Integer size = 10;
        String createdByIds = "1,2";
        String changedByIds = "1,2";
        String sort = "id,desc";

        PaginatedResponse<UserDto> response = new PaginatedResponse<>(List.of(testuserDto), 1,
                1, 0, new UserFilterValues());

        when(userService.getUsers(search, isActive, locationIds, roleIds, createdAtStart, createdAtEnd,
                changedAtStart, changedAtEnd, createdByIds, changedByIds, page, size, sort)).thenReturn(response);

        mockMvc.perform(get("/users")
                        .param("search", search)
                        .param("isActive", String.valueOf(isActive))
                        .param("locationIds", locationIds)
                        .param("roleIds", roleIds)
                        .param("createdAtStart", String.valueOf(createdAtStart))
                        .param("createdAtEnd", String.valueOf(createdAtEnd))
                        .param("changedAtStart", String.valueOf(createdAtStart))
                        .param("changedAtEnd", String.valueOf(createdAtEnd))
                        .param("createdByIds", createdByIds)
                        .param("changedByIds", changedByIds)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value(testuserDto.getUsername()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(response.getTotalElements()))
                .andExpect(jsonPath("$.currentPage").value(response.getCurrentPage()));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldUpdateUser() throws Exception {
        when(userService.updateUser(any(Integer.class), any(UserDto.class))).thenReturn(testuserDto);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testuserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("bill"));

        verify(userService, times(1)).updateUser(any(Integer.class), any(UserDto.class));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(anyInt());
    }
}
