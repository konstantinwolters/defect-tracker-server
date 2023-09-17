package com.example.defecttrackerserver.security;

import com.example.defecttrackerserver.core.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Implementation of {@link GrantedAuthority}.
 */
@RequiredArgsConstructor
public class SecurityRole implements GrantedAuthority {

    private final Role role;
    @Override
    public String getAuthority() {
        return role.getName();
    }
}
