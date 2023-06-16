package com.example.defecttrackerserver.config;

import com.example.defecttrackerserver.core.action.Action;
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
                .addMappings(mapper -> mapper.using(actionToIdConverter).map(User::getAssignedActions, UserDto::setAssignedActions));

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
}
