package com.example.defecttrackerserver.core.user.user.dto;

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
    private String location;
    private Set<String> roles = new HashSet<>();
    private Set<Integer> assignedActions = new HashSet<>();
}
