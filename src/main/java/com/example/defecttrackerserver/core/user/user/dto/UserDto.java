package com.example.defecttrackerserver.core.user.user.dto;

import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto  {
    private Integer id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String mail;
    private LocationDto location;
    private Set<RoleDto> roles = new HashSet<>();
    private Set<Integer> assignedActions = new HashSet<>();
}
