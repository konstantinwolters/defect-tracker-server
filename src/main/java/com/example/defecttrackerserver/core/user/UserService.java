package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.user.role.Role;

import java.util.List;
import java.util.Set;

public interface UserService {

    UserDto saveUser(UserDto user);
    UserDto getUserById(Integer id);
    List<UserDto> getUsers();
    UserDto updateUser(UserDto user);
    void deleteUser(Integer id);
    UserDto getUserByUsername(String username);
    Set<Role> getRoles(Integer id);
}
