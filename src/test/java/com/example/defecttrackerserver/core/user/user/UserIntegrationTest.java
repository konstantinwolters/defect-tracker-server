package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserIntegrationTest extends BaseIntegrationTest {
    String URL = "/users";

    @BeforeEach
    void setUp() {
        commonSetup();

        setUpRole("ROLE_USER");
    }

    @Test
    @Transactional
    void shouldSaveUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setCustomId("testCustomId");
        userDto.setPassword("testPassword");
        userDto.setMail("testMail");
        userDto.setIsActive(true);
        userDto.setFirstName("testFirstname");
        userDto.setLastName("testLastname");
        userDto.setLocation("testLocation");
        userDto.setRoles(Set.of("ROLE_USER"));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        User savedUser = userRepository.findAll().get(1); // get(1) because BaseIntegrationTest already inserts a user

        assertEquals(userDto.getUsername(), savedUser.getUsername());
        assertEquals(userDto.getCustomId(), savedUser.getCustomId());
        assertEquals(userDto.getMail(), savedUser.getMail());
        assertEquals(userDto.getFirstName(), savedUser.getFirstName());
        assertEquals(userDto.getLastName(), savedUser.getLastName());
        assertEquals(userDto.getLocation(), savedUser.getLocation().getName());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        UserDto userDto = new UserDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        UserDto userDto = new UserDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetUserId() throws Exception{
        User user = setUpUser("testUser", "email2", roleADMIN, location);

        UserDto userDto = userMapper.mapToDto(user);

        mockMvc.perform(get(URL + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    @Transactional
    void shouldGetAllUsers() throws Exception{
        setUpUser("testUser2", "email2", roleADMIN, location);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.currentPage").value(0));
    }


    @Test
    @Transactional
    void shouldGetUsersFilteredByLocation() throws Exception{
        Location location2 = setUpLocation("testLocation2");

        setUpUser("testUser2", "email2", roleQA, location2);

        mockMvc.perform(get(URL)
                        .param("location", String.valueOf(location2.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.currentPage").value(0))
                // Check if the first element of content has location object with the given id
                .andExpect(jsonPath("$.content[1].location").value(location2.getName()));
    }

    @Test
    @Transactional
    void shouldGetUsersFilteredBySearchTerm() throws Exception{

        String searchUsername = "Wolfgang";
        setUpUser(searchUsername, "mail2", roleQA, location);

        mockMvc.perform(get(URL)
                        .param("search", searchUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                // Check if the first elements description content contains the searchterm
                .andExpect(jsonPath("$.content[0].username").value(searchUsername));
    }

    @Test
    @Transactional
    void shouldUpdateUser() throws Exception{
        User user = setUpUser("testUser", "email2", roleADMIN, location);

        UserDto userDto = userMapper.mapToDto(user);
        userDto.setUsername("UpdatedTestUser");

        mockMvc.perform(put(URL + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        Optional<User> savedUser =
                userRepository.findById(user.getId());

        assertTrue(savedUser.isPresent());
        assertEquals(userDto.getUsername(), savedUser.get().getUsername());
    }

    @Test
    @Transactional
    void shouldDeleteUserById() throws Exception{
        User user = setUpUser("testUser", "email2", roleADMIN, location);

        mockMvc.perform(delete(URL + "/" + user.getId()))
                .andExpect(status().isNoContent());
    }
}
