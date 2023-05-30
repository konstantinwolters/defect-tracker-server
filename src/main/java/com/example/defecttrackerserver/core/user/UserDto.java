package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto  {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    private Location location;
    //private Set<Role> roles = user.getRoles();
}
