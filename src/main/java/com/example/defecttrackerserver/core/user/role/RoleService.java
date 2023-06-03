package com.example.defecttrackerserver.core.user.role;

import java.util.List;

public interface RoleService {
    Role getRoleById(Integer i);
    List<Role> getRoles();
}
