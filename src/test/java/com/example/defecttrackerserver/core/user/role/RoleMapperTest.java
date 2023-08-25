package com.example.defecttrackerserver.core.user.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleMapperTest {

    @InjectMocks
    private RoleMapper roleMapper;

    private Role role;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setId(1);
        role.setName("Test");
    }

    @Test
    void shouldReturnMappedRoleDto() {

        RoleDto mappedRoleDto = roleMapper.mapToDto(role);

        assertEquals(role.getId(), mappedRoleDto.getId());
        assertEquals(role.getName(), mappedRoleDto.getName());
    }
}

