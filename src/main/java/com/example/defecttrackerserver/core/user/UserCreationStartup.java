package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//Just for Tesing-Purposes. Gets removed later
@Component
@RequiredArgsConstructor
public class UserCreationStartup implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        Role role = new Role();
        role.setName("ROLE_READ");


        User user = new User();
        user.setUsername("bill");
        user.setPassword(passwordEncoder.encode("12345"));

        user.addRole(role);

        userRepository.save(user);
    }
}
