package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.core.user.user.userException.UserExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final LocationRepository locationRepository;
    private final RoleRepository roleRepository;
    private final ActionRepository actionRepository;
    private final UserRepository userRepository;

    public User mapToEntity(UserDto userDto, User user){
        checkDuplicateUserEntries(userDto);

        Location location = getLocationByName(userDto.getLocation());
        Set<Role> roles = Optional.ofNullable(userDto.getRoles())
                .orElse(Collections.emptySet())
                .stream()
                .map(this::getRoleByName)
                .collect(Collectors.toSet());

        Set<Action> actions =  Optional.ofNullable(userDto.getAssignedActions())
                .orElse(Collections.emptySet())
                .stream()
                .map(this::getActionById)
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
        user.setCreatedBy(userDto.getCreatedBy());
        user.setChangedBy(userDto.getChangedBy());
        user.setLocation(location);
        user.setRoles(roles);
        user.setAssignedActions(actions);

        return user;
    }

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(()-> new EntityNotFoundException("Role not found with name: "
                        + roleName));
    }


    private Action getActionById(Integer actionId) {
        return actionRepository.findById(actionId)
                .orElseThrow(()-> new EntityNotFoundException("Action not found with id: "
                        + actionId));

    }

    private Location getLocationByName(String name){
        return locationRepository.findByName(name)
                .orElseThrow(()-> new EntityNotFoundException("Location not found with name: "
                        + name));
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
        userDto.setCreatedBy(user.getCreatedBy());
        userDto.setChangedBy(user.getChangedBy());
        userDto.setRoles(roles);
        userDto.setAssignedActions(actions);

        return userDto;
    }

    public void checkDuplicateUserEntries(UserDto userDto) {

        if(userDto.getId() == null) {
            checkForUsernameAndMailUsage(userDto, null);
        }else {
            Optional<User> existingUser = userRepository.findById(userDto.getId());
            //if user exists = update operation, otherwise = save operation
            if (existingUser.isPresent()) {
                checkForUsernameAndMailUsage(userDto, existingUser.get().getId());
            } else {
                checkForUsernameAndMailUsage(userDto, null);
            }
        }
    }

    public void checkForUsernameAndMailUsage(UserDto userDto, Integer userId) {
        Optional<User> existingUserByUsername = userRepository.findByUsername(userDto.getUsername());
        Optional<User> existingUserByMail = userRepository.findByMail(userDto.getMail());

        if(existingUserByUsername.isPresent() && !existingUserByUsername.get().getId().equals(userId)) {
            throw new UserExistsException("Username already exists: " + userDto.getUsername());
        }

        if(existingUserByMail.isPresent() && !existingUserByMail.get().getId().equals(userId)) {
            throw new UserExistsException("Mail already exists: " + userDto.getMail());
        }
    }
}
