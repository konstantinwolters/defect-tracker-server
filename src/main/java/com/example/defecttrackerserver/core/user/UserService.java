package com.example.defecttrackerserver.core.user;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto user);
    UserDto getUserById(Integer id);
    List<UserDto> getUsers();
    UserDto updateUser(UserDto user);
    void deleteUser(Integer id);
    UserDto getUserByUsername(String username);
}
