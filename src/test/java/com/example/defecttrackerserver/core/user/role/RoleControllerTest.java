package com.example.defecttrackerserver.core.user.role;

import com.example.defecttrackerserver.config.SecurityConfig;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(RoleController.class)
@Import(SecurityConfig.class)
public class RoleControllerTest {

    @MockBean
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    private Role role;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setName("ADMIN");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldReturnRoleById() throws Exception {
        when(roleService.getRoleById(1)).thenReturn(role);

        mockMvc.perform(get("/role/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldReturnAllRoles() throws Exception {

        when(roleService.getRoles()).thenReturn(Arrays.asList(role));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("ADMIN"));
    }


}
