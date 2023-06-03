package com.example.defecttrackerserver.core.user.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Role not found"));
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
