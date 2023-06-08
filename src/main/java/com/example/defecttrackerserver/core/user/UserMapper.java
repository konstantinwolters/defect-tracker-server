//package com.example.defecttrackerserver.core.user;
//
//import com.example.defecttrackerserver.core.action.Action;
//import com.example.defecttrackerserver.core.action.ActionRepository;
//import com.example.defecttrackerserver.core.location.Location;
//import com.example.defecttrackerserver.core.location.LocationRepository;
//import com.example.defecttrackerserver.core.user.role.Role;
//import com.example.defecttrackerserver.core.user.role.RoleRepository;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.Converter;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class UserMapper {
//    private final ModelMapper modelMapper;
//    private final ActionRepository actionRepository;
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//    private final LocationRepository locationRepository;
//
//    @PostConstruct
//    public void init() {
//        setupUserToUserDtoMapping();
//        setupUserDtoToUserMapping();
//    }
//
//    private void setupUserToUserDtoMapping() {
//        modelMapper.createTypeMap(User.class, UserDto.class)
//                .addMappings(mapper -> {
//                    mapper.using(ctx -> ((Set<Role>) ctx.getSource()).stream()
//                                    .map(Role::getName)
//                                    .collect(Collectors.toSet()))
//                                    .map(User::getRoles, UserDto::setRoles);
//                    mapper.using(ctx -> ((Set<Action>) ctx.getSource()).stream()
//                                    .map(Action::getId)
//                                    .collect(Collectors.toSet()))
//                                    .map(User::getCreatedActions, UserDto::setCreatedActions);
//                    mapper.map(user -> user.getLocation().getName(), UserDto::setLocation);
//                });
//    }
//
//    private void setupUserDtoToUserMapping() {
//        Converter<Set<Integer>, Set<Action>> actionsConverter = getActionsConverter();
//        Converter<Set<String>, Set<Role>> rolesConverter = getRolesConverter();
//        Converter<String, Location> locationConverter = getLocationConverter();
//
//        modelMapper.createTypeMap(UserDto.class, User.class)
//                .addMappings(
//                        mapper -> mapper.using(rolesConverter)
//                                .map(UserDto::getRoles, User::setRoles))
//                .addMappings(
//                        mapper -> mapper.using(actionsConverter)
//                                .map(UserDto::getCreatedActions, User::setCreatedActions))
//                .addMappings(mapper -> mapper.using(locationConverter)
//                        .map(UserDto::getLocation, User::setLocation));
//    }
//
//    private Converter<Set<Integer>, Set<Action>> getActionsConverter() {
//        return ctx -> {
//            if (ctx.getSource() == null) {
//                return null;
//            } else {
//                return ctx.getSource().stream()
//                        .map(actionId -> {
//                            Action action = actionRepository.findById(actionId)
//                                    .orElseThrow(() -> new EntityNotFoundException("Action not found"));
//
//                            User user = userRepository.findById(action.getCreatedBy().getId())
//                                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//                            action.setCreatedBy(user);
//
//                            return action;
//                        })
//                        .collect(Collectors.toSet());
//            }
//        };
//    }
//
//    private Converter<Set<String>, Set<Role>> getRolesConverter() {
//        return ctx -> {
//            if (ctx.getSource() == null) {
//                return null;
//            } else {
//                return ctx.getSource().stream()
//                        .map(roleName -> roleRepository.findByName(roleName)
//                                .orElseThrow(() -> new EntityNotFoundException("Role not found")))
//                        .collect(Collectors.toSet());
//            }
//        };
//    }
//
//    public Converter<String, Location> getLocationConverter() {
//        return ctx -> {
//            if (ctx.getSource() == null) {
//                return null;
//            } else {
//                return locationRepository.findByName(ctx.getSource())
//                        .orElseThrow(() -> new EntityNotFoundException("Location not found"));
//            }
//        };
//    }
//}
