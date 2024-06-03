package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.response.PaginatedResponse;

import java.time.LocalDate;

/**
 * Service interface for managing {@link User}.
 */
interface UserService {
    UserDto saveUser(UserDto userDto);
    UserDto getUserById(Integer id);
    PaginatedResponse<UserDto> getUsers(
            String searchTerm,
            Boolean isActive,
            String locationIds,
            String roleIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            String createdByIds,
            String changedByIds,
            Integer page,
            Integer size,
            String sort
    );
    UserDto updateUser(Integer userId, UserDto userDto);
    boolean deleteUser(Integer id);
    void deactivateUser(Integer id);
    UserDto getUserByUsername(String username);
}
