package com.example.defecttrackerserver.core.user.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("TEST");
    }

    @Test
    void shouldReturnRoleById() {
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(role));

        Role result = roleService.getRoleById(1);

        assertNotNull(result);
        assertEquals(role.getId(), result.getId());
        assertEquals(role.getName(), result.getName());
    }

    @Test
    void shouldReturnALlRoles() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));

        List<Role> result = roleService.getRoles();

        assertNotNull(result);
        assertEquals(role.getId(), result.get(0).getId());
        assertEquals(role.getName(), result.get(0).getName());
    }
}
