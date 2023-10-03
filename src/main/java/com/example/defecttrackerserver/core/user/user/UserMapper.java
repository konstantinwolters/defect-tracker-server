package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {
    private final UserRepository userRepository;
    private final EntityService entityService;

    public User mapToEntity(UserDto userDto, User user){
        checkDuplicateUserEntries(userDto);

        Location location = entityService.getLocationByName(userDto.getLocation());
        Set<Role> roles = Optional.ofNullable(userDto.getRoles())
                .orElse(Collections.emptySet())
                .stream()
                .map(entityService::getRoleByName)
                .collect(Collectors.toSet());

        Set<Action> actions =  Optional.ofNullable(userDto.getAssignedActions())
                .orElse(Collections.emptySet())
                .stream()
                .map(entityService::getActionById)
                .collect(Collectors.toSet());

        user.setCustomId(userDto.getCustomId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMail(userDto.getMail());
        user.setIsActive(userDto.getIsActive());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setChangedAt(userDto.getChangedAt());
        user.setCreatedById(userDto.getCreatedBy());
        user.setChangedById(userDto.getChangedBy());
        user.setLocation(location);
        user.setRoles(roles);
        user.setAssignedActions(actions);

        return user;
    }

    public UserDto mapToDto(User user){
        Set<String> roles = Optional.ofNullable(user.getRoles())
                .orElse(Collections.emptySet())
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<Integer> actions = Optional.ofNullable(user.getAssignedActions())
                .orElse(Collections.emptySet())
                .stream()
                .map(Action::getId)
                .collect(Collectors.toSet());

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setCustomId(user.getCustomId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(null);
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMail(user.getMail());
        userDto.setLocation(user.getLocation().getName());
        userDto.setIsActive(user.getIsActive());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setChangedAt(user.getChangedAt());
        userDto.setCreatedBy(user.getCreatedById());
        userDto.setChangedBy(user.getChangedById());
        userDto.setRoles(roles);
        userDto.setAssignedActions(actions);

        return userDto;
    }

    public void checkDuplicateUserEntries(UserDto userDto) {
        if(userDto.getId() == null) {
            checkForMailUsage(userDto, null);
            checkForUsernameUsage(userDto, null);
        }else {
            Optional<User> existingUser = userRepository.findById(userDto.getId());
            //if user exists = update operation, otherwise = save operation
            if (existingUser.isPresent()) {
                checkForMailUsage(userDto, existingUser.get().getId());
                checkForUsernameUsage(userDto, existingUser.get().getId());
            } else {
                checkForMailUsage(userDto, null);
                checkForUsernameUsage(userDto, null);
            }
        }
    }

    public void checkForMailUsage(UserDto userDto, Integer userId) {
        Optional<User> existingUserByMail = userRepository.findByMail(userDto.getMail());

        if(existingUserByMail.isPresent() && !existingUserByMail.get().getId().equals(userId)) {
            throw new DuplicateKeyException("Mail already exists: " + userDto.getMail());
        }
    }

    public void checkForUsernameUsage(UserDto userDto, Integer userId){
        Optional<User> existingUserByUsername = userRepository.findByUsername(userDto.getUsername());

        if(existingUserByUsername.isPresent() && !existingUserByUsername.get().getId().equals(userId)) {
            throw new DuplicateKeyException("Username already exists: " + userDto.getUsername());
        }
    }
}
