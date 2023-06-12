package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import com.example.defecttrackerserver.core.user.user.userException.UserExistsException;
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

    public User map(UserDto userDto){
        User user = new User();
        user.setId(null);
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMail(userDto.getMail());

        user.setLocation(locationRepository.findById(
                userDto.getLocation().getId())
                .orElseThrow(()-> new EntityNotFoundException("Location not found with id: "
                        + userDto.getLocation().getId())));

        if(userDto.getRoles() == null || userDto.getRoles().isEmpty()){
            user.setRoles(new HashSet<>());
        } else {
           user.setRoles(userDto.getRoles().stream()
                   .map(roleDto -> roleRepository.findById(roleDto.getId())
                           .orElseThrow(()-> new EntityNotFoundException("Role not found with id: "
                                   + roleDto.getId())))
                   .collect(Collectors.toSet()));
        }

        if(userDto.getAssignedActions() == null || userDto.getAssignedActions().isEmpty()) {
            user.setAssignedActions(new HashSet<>());
        } else {
            user.setAssignedActions(userDto.getAssignedActions().stream()
                    .map(actionDto -> actionRepository.findById(actionDto.getId())
                            .orElseThrow(()-> new EntityNotFoundException("Action not found with id: "
                                    + actionDto.getId())))
                    .collect(Collectors.toSet()));
        }
        return user;
    }

    public void checkNullOrEmptyFields(UserDto userDto) {
        if(userDto.getUsername() == null
                || userDto.getUsername().isBlank())
            throw new IllegalArgumentException("Username must not be null or empty");
        if(userDto.getMail() == null
                || userDto.getMail().isBlank())
            throw new IllegalArgumentException("Mail must not be null or empty");
        if(userDto.getPassword() == null
                || userDto.getPassword().isBlank())
            throw new IllegalArgumentException("Password must not be null or empty");
        if(userDto.getLocation() == null)
            throw new IllegalArgumentException("Location must not be null");
    }

    public void checkDuplicateUserEntries(UserDto userDto){

        Optional<User> existingUser = userRepository.findById(userDto.getId());

        if(existingUser.isPresent()){
            if(userRepository.findByUsername(userDto.getUsername()).isPresent()
                    && !existingUser.get().getId().equals(userDto.getId()))
                    throw new UserExistsException("Username already exists: " + userDto.getUsername());

            if(userRepository.findByMail(userDto.getMail()).isPresent()
                    && !existingUser.get().getId().equals(userDto.getId()))
                    throw new UserExistsException("Mail already exists: " + userDto.getMail());
        }else {
            if (userRepository.findByUsername(userDto.getUsername()).isPresent())
                throw new UserExistsException("Username already exists: " + userDto.getUsername());

            if (userRepository.findByMail(userDto.getMail()).isPresent())
                throw new UserExistsException("Mail already exists: " + userDto.getMail());
        }
    }

}
