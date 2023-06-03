package com.example.defecttrackerserver.core.user.role;

import com.example.defecttrackerserver.core.user.User;
import com.example.defecttrackerserver.core.user.UserDto;
import com.example.defecttrackerserver.core.user.UserRepository;
import com.example.defecttrackerserver.core.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
        verify(roleRepository, times(1)).findById(anyInt());
    }

    @Test
    void shouldReturnALlRoles() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));

        List<Role> result = roleService.getRoles();

        assertNotNull(result);
        assertEquals(role.getId(), result.get(0).getId());
        assertEquals(role.getName(), result.get(0).getName());
        verify(roleRepository, times(1)).findAll();
    }
}
