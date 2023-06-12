package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.user.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);
    UserDto getUserById(Integer id);
    List<UserDto> getAllUsers();
    UserDto updateUser(UserDto userDto);
    void deleteUser(Integer id);
    UserDto getUserByUsername(String username);
}
