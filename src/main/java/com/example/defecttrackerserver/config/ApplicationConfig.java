package com.example.defecttrackerserver.config;

import com.example.defecttrackerserver.core.user.User;
import com.example.defecttrackerserver.core.user.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.typeMap(UserDto.class, User.class).addMappings(mapper -> mapper.skip(User::setId));
        return modelMapper;
    }
}
