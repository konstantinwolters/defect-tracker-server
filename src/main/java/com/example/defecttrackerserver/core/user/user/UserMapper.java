package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.userException.UserExistsException;
import com.example.defecttrackerserver.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
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

        user.setLocation(locationRepository.findByName(
                userDto.getLocation())
                .orElseThrow(()-> new EntityNotFoundException("Location not found with name: "
                        + userDto.getLocation())));

        if(userDto.getRoles() != null){
           user.setRoles(userDto.getRoles().stream()
                   .map(role -> roleRepository.findByName(role)
                           .orElseThrow(()-> new EntityNotFoundException("Role not found with name: "
                                   + role)))
                   .collect(Collectors.toSet()));
        }

        if(userDto.getAssignedActions() != null ) {
            user.setAssignedActions(userDto.getAssignedActions().stream()
                    .map(actionId -> actionRepository.findById(actionId)
                            .orElseThrow(()-> new EntityNotFoundException("Action not found with id: "
                                    + actionId)))
                    .collect(Collectors.toSet()));
        }
        return user;
    }

    public UserDto mapToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
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
        if(user.getRoles() != null) {
            userDto.setRoles(new HashSet<>(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet())));
        }
        if(user.getAssignedActions() != null) {
            userDto.setAssignedActions(new HashSet<>(user.getAssignedActions().stream()
                    .map(Action::getId)
                    .collect(Collectors.toSet())));
        }
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
