package com.example.defecttrackerserver.core.user.user.dto;

import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserCreationDto {
    private String username;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    private LocationDto location;
    private Set<RoleDto> roles = new HashSet<>();
}
