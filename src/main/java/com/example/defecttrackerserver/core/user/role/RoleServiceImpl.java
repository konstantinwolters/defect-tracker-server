package com.example.defecttrackerserver.core.user.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link RoleService}.
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto getRoleById(Integer id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Role not found"));
        return roleMapper.mapToDto(role);
    }

    @Override
    public List<RoleDto> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::mapToDto).toList();
    }
}
