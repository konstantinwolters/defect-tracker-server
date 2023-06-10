package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;
    private final ActionRepository actionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @PostConstruct
    public void init() {
        setupUserToUserDtoMapping();
        setupUserDtoToUserMapping();
        setupLocationToLocationDtoMapping();
        setupRoleToRoleDtoMapping();
        //setupActionToActionDtoMapping(); -> Does bad things...
    }

    private void setupUserToUserDtoMapping() {
        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMappings(mapper -> {
                    mapper.using(ctx -> ((Set<Role>) ctx.getSource()).stream()
                                    .map(role -> modelMapper.map(role, RoleDto.class))
                                    .collect(Collectors.toSet()))
                                    .map(User::getRoles, UserDto::setRoles);
                    mapper.using(ctx -> ((Set<Action>) ctx.getSource()).stream()
                                    .map(action -> modelMapper.map(action, ActionDto.class))
                                    .collect(Collectors.toSet()))
                                    .map(User::getAssignedActions, UserDto::setAssignedActions);
                    mapper.map(User::getId, UserDto::setId);
                    mapper.map(User::getUsername, UserDto::setUsername);
                    mapper.map(User::getFirstName, UserDto::setFirstName);
                    mapper.map(User::getLastName, UserDto::setLastName);
                    mapper.map(User::getMail, UserDto::setMail);
                    mapper.map(User::getLocation, UserDto::setLocation);
                });
    }

    private void setupLocationToLocationDtoMapping(){
        modelMapper.createTypeMap(Location.class, LocationDto.class)
                .addMappings(mapper -> {
                    mapper.map(Location::getId, LocationDto::setId);
                    mapper.map(Location::getName, LocationDto::setName);
                });
    }

    private void setupRoleToRoleDtoMapping(){
        modelMapper.createTypeMap(Role.class, RoleDto.class)
                .addMappings(mapper -> {
                    mapper.map(Role::getId, RoleDto::setId);
                    mapper.map(Role::getName, RoleDto::setName);
                });
    }

    private void setupActionToActionDtoMapping() {
        modelMapper.createTypeMap(Action.class, ActionDto.class)
                .addMappings(mapper -> {
                    mapper.map(Action::getId, ActionDto::setId);
                    mapper.map(Action::getDescription, ActionDto::setDescription);
                    mapper.map(Action::getCreatedBy, ActionDto::setCreatedBy);
                    mapper.using(ctx -> ((Set<User>) ctx.getSource()).stream()
                                    .map(user -> modelMapper.map(user, UserDto.class))
                                    .collect(Collectors.toSet()))
                            .map(Action::getAssignedUsers, ActionDto::setAssignedUsers);
                    mapper.map(Action::getCreatedOn , ActionDto::setCreatedOn);
                    mapper.map(Action::getCreatedBy , ActionDto::setCreatedBy);
                    mapper.map(Action::getDueDate , ActionDto::setDueDate);
                    mapper.map(Action::getDefect , ActionDto::setDefect);
                    mapper.map(Action::getIsCompleted , ActionDto::setIsCompleted);
                });
    }

    private void setupUserDtoToUserMapping() {
        Converter<Set<ActionDto>, Set<Action>> actionsConverter = getActionsConverter();
        Converter<Set<RoleDto>, Set<Role>> rolesConverter = getRolesConverter();
        Converter<LocationDto, Location> locationConverter = getLocationConverter();

        modelMapper.createTypeMap(UserDto.class, User.class)
                .addMappings(
                        mapper -> mapper.using(rolesConverter)
                                .map(UserDto::getRoles, User::setRoles))
                .addMappings(
                        mapper -> mapper.using(actionsConverter)
                                .map(UserDto::getAssignedActions, User::setAssignedActions))
                .addMappings(mapper -> mapper.using(locationConverter)
                        .map(UserDto::getLocation, User::setLocation))
                .addMappings(mapper -> mapper.map(UserDto::getId, User::setId))
                .addMappings(mapper -> mapper.map(UserDto::getUsername, User::setUsername))
                .addMappings(mapper -> mapper.map(UserDto::getFirstName, User::setFirstName))
                .addMappings(mapper -> mapper.map(UserDto::getLastName, User::setLastName))
                .addMappings(mapper -> mapper.map(UserDto::getMail, User::setMail))
                .addMappings(mapper -> mapper.map(UserDto::getPassword, User::setPassword));
    }

    private Converter<Set<ActionDto>, Set<Action>> getActionsConverter() {
        return ctx -> {
            if (ctx.getSource() == null) {
                return null;
            } else {
                return ctx.getSource().stream()
                        .map(actionDto -> {
                            Action action = actionRepository.findById(actionDto.getId())
                                    .orElseThrow(() -> new EntityNotFoundException("Action not found"));

                            User user = userRepository.findById(action.getCreatedBy().getId())
                                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

                            action.getAssignedUsers().add(user);

                            return action;
                        })
                        .collect(Collectors.toSet());
            }
        };
    }

    private Converter<Set<RoleDto>, Set<Role>> getRolesConverter() {
        return ctx -> {
            if (ctx.getSource() == null) {
                return null;
            } else {
                return ctx.getSource().stream()
                        .map(roleDto -> roleRepository.findById(roleDto.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Role not found")))
                        .collect(Collectors.toSet());
            }
        };
    }

    public Converter<LocationDto, Location> getLocationConverter() {
        return ctx -> {
            if (ctx.getSource() == null) {
                return null;
            } else {
                return locationRepository.findById(ctx.getSource().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Location not found"));
            }
        };
    }
}
