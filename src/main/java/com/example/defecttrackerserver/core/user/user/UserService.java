package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);
    UserDto getUserById(Integer id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Integer userId, UserDto userDto);
    void deleteUser(Integer id);
    UserDto getUserByUsername(String username);
}
