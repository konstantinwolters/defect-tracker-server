package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreationStartup implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {

        Role role = new Role();
        role.setName("READ");


        User user = new User();
        user.setUsername("bill");
        user.setPassword("12345");

        user.addRole(role);

        userRepository.save(user);
    }
}
