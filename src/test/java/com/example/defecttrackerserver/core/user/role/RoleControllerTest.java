package com.example.defecttrackerserver.core.user.role;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
public class RoleControllerTest extends BaseControllerTest {

    @Autowired
    private RoleController roleController;

    @MockBean
    private RoleService roleService;

    private Role role;
    private RoleDto roleDto;

    @Override
    protected Object getController() {
        return roleController;
    }

    @BeforeEach
    public void setUp(){
        super.setUp();
        role = TestHelper.setUpRole();
        roleDto = TestHelper.setUpRoleDto();
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldReturnRoleById() throws Exception {
        when(roleService.getRoleById(1)).thenReturn(roleDto);

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testRole"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldReturnAllRoles() throws Exception {
        when(roleService.getRoles()).thenReturn(Arrays.asList(roleDto));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("testRole"));
    }
}
