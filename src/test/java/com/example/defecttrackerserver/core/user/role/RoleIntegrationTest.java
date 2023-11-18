package com.example.defecttrackerserver.core.user.role;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleIntegrationTest extends BaseIntegrationTest {
    String URL = "/roles";

    @BeforeEach
    void setUp() {
        commonSetup();
    }

    @Test
    @Transactional
    void shouldGetRoleId() throws Exception{
        Role role = setUpRole("testRole");

        RoleDto roleDto = roleMapper.mapToDto(role);

        mockMvc.perform(get(URL + "/" + role.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roleDto)));
    }

    @Test
    @Transactional
    void shouldGetAllRoles() throws Exception{

        MvcResult result = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<?> list = mapper.readValue(content, List.class);

        assertEquals(2, list.size());
    }
}
