package com.example.defecttrackerserver.core.user.role;

import java.util.List;

public interface RoleService {
    RoleDto getRoleById(Integer i);
    List<RoleDto> getRoles();
}
