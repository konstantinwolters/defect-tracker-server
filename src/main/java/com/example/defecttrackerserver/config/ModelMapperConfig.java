package com.example.defecttrackerserver.config;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.user.User;
import com.example.defecttrackerserver.core.user.UserDto;
import com.example.defecttrackerserver.core.user.UserRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
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
public class ModelMapperConfig {
    private final ModelMapper modelMapper;
    private final ActionRepository actionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        setupUserToUserDtoMapping();
        setupUserDtoToUserMapping();
    }

    private void setupUserToUserDtoMapping() {
        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMappings(mapper -> {
                    mapper.using(ctx -> ((Set<Role>) ctx.getSource()).stream()
                                    .map(Role::getId)
                                    .collect(Collectors.toSet()))
                            .map(User::getRoles, UserDto::setRoles);
                    mapper.using(ctx -> ((Set<Action>) ctx.getSource()).stream()
                                    .map(Action::getId)
                                    .collect(Collectors.toSet()))
                            .map(User::getCreatedActions, UserDto::setCreatedActions);
                });
    }

    private void setupUserDtoToUserMapping() {
        Converter<Set<Integer>, Set<Action>> actionsConverter = getActionsConverter();
        Converter<Set<Integer>, Set<Role>> rolesConverter = getRolesConverter();

        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(m -> m.using(rolesConverter).map(UserDto::getRoles, User::setRoles))
                .addMappings(m -> m.using(actionsConverter).map(UserDto::getCreatedActions, User::setCreatedActions));
    }

    private Converter<Set<Integer>, Set<Action>> getActionsConverter() {
        return ctx -> {
            if (ctx.getSource() == null) {
                return null;
            } else {
                return ctx.getSource().stream()
                        .map(actionId -> {
                            Action action = actionRepository.findById(actionId)
                                    .orElseThrow(() -> new EntityNotFoundException("Action not found"));

                            User user = userRepository.findById(action.getCreatedBy().getId())
                                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

                            action.setCreatedBy(user);

                            return action;
                        })
                        .collect(Collectors.toSet());
            }
        };
    }

    private Converter<Set<Integer>, Set<Role>> getRolesConverter() {
        return ctx -> {
            if (ctx.getSource() == null) {
                return null;
            } else {
                return ctx.getSource().stream()
                        .map(roleId -> roleRepository.findById(roleId)
                                .orElseThrow(() -> new EntityNotFoundException("Role not found")))
                        .collect(Collectors.toSet());
            }
        };
    }
}
