package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.location.Location;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class UserDto  {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    private Location location;
    private Set<Integer> roles = new HashSet<>();
    private Set<Integer> createdActions = new HashSet<>();
}
