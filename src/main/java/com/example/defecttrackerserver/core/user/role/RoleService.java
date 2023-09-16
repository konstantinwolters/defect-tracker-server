package com.example.defecttrackerserver.core.user.role;

import java.util.List;

/**
 * Service interface for managing {@link Role}.
 */
public interface RoleService {
    RoleDto getRoleById(Integer i);
    List<RoleDto> getRoles();
}
