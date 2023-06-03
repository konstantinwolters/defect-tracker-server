package com.example.defecttrackerserver.core.user.role;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/role/{id}")
    public Role getRole(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }
}
