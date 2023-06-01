package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//Just for Testing-Purposes. Gets removed later
@Component
@RequiredArgsConstructor
public class UserCreationStartup implements ApplicationRunner {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        Role role = new Role();
        role.setName("ROLE_READ");

        Location location = new Location();
        location.setName("Texas");


        User user = new User();
        user.setUsername("bill");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setLocation(location);
        user.addRole(role);

        location.addUser(user);
        locationRepository.save(location);

    }
}
