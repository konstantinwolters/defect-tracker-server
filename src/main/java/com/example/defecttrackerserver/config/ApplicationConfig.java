package com.example.defecttrackerserver.config;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(User.class, UserDto.class)
                .addMappings( mapper -> {
                    mapper.skip(UserDto::setPassword);})
                .addMappings(mapper -> mapper.using(actionToIdConverter).map(User::getAssignedActions, UserDto::setAssignedActions))
                .addMappings(mapper -> mapper.using(LocationToStringConverter).map(User::getLocation, UserDto::setLocation))
                .addMappings(mapper -> mapper.using(RoleToStringConverter).map(User::getRoles, UserDto::setRoles));

        modelMapper.typeMap(Action.class, ActionDto.class)
                .addMappings( mapper -> mapper.using(DefectToIdConverter).map(Action::getDefect, ActionDto::setDefect));

        return modelMapper;
    }

    Converter<Set<Action>, Set<Integer>> actionToIdConverter = new AbstractConverter<Set<Action>, Set<Integer>>() {
        @Override
        protected Set<Integer> convert(Set<Action> source) {
            if (source == null) {
                return null;
            }
            return source.stream()
                    .map(Action::getId)
                    .collect(Collectors.toSet());
        }
    };

    Converter<Location, String> LocationToStringConverter = context -> {
        Location source = context.getSource();
        return source == null ? null : source.getName();
    };


    Converter<Set<Role>, Set<String>> RoleToStringConverter = new AbstractConverter<Set<Role>, Set<String>>() {
        @Override
        protected Set<String> convert(Set<Role> source) {
            if (source == null) {
                return null;
            }
            return source.stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
        }
    };

    Converter<Defect, Integer> DefectToIdConverter = context -> {
        Defect source = context.getSource();
        return source == null ? null : source.getId();
    };
}
