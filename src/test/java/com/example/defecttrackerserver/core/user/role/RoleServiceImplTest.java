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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private RoleDto roleDto;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("testRole");

        roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName("testRole");
    }

    @Test
    void shouldReturnRoleById() {
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(role));
        when(roleMapper.mapToDto(any(Role.class))).thenReturn(roleDto);

        RoleDto result = roleService.getRoleById(1);

        assertNotNull(result);
        assertEquals(role.getId(), result.getId());
        assertEquals(role.getName(), result.getName());
    }

    @Test
    void shouldReturnALlRoles() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));
        when(roleMapper.mapToDto(any(Role.class))).thenReturn(roleDto);

        List<RoleDto> result = roleService.getRoles();

        assertNotNull(result);
        assertEquals(role.getId(), result.get(0).getId());
        assertEquals(role.getName(), result.get(0).getName());
    }
}
