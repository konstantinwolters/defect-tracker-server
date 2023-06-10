package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.user.role.RoleDto;

import java.util.Set;

public class UserUtils {

    public static void checkNullValues(String username, String mail, String password, Set<RoleDto> roles, LocationDto location) {
        if(username == null
                || username.isBlank())
            throw new IllegalArgumentException("Username must not be null or empty");
        if(mail == null
                || mail.isBlank())
            throw new IllegalArgumentException("Mail must not be null or empty");
        if(password == null
                || password.isBlank())
            throw new IllegalArgumentException("Password must not be null or empty");
        if(roles == null
                || roles.isEmpty())
            throw new IllegalArgumentException("Roles must not be null or empty");
        if(location == null)
            throw new IllegalArgumentException("Location must not be null");
    }
}
