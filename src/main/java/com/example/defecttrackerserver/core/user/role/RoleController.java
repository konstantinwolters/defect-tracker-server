package com.example.defecttrackerserver.core.user.role;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/{id}")
    public Role getRole(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @GetMapping()
    public List<Role> getRoles() {
        return roleService.getRoles();
    }
}
