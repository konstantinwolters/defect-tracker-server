package com.example.defecttrackerserver.core.user.role;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDto mapToDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        return roleDto;
    }
}
