package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

//Just for Testing-Purposes. Gets removed later
@Component
@RequiredArgsConstructor
public class UserCreationStartup implements ApplicationRunner {

    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        Role role = new Role();
        role.setName("ROLE_ADMIN");

        Location location = new Location();
        location.setName("Texas");

        Action action = new Action();
        action.setDescription("Test Action");
        action.setIsCompleted(false);
        action.setDueDate(LocalDate.now());
        action.setCreatedOn(LocalDateTime.now());


        User user = new User();
        user.setUsername("bill");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setMail("test@test.de");
        user.setLocation(location);
        user.addRole(role);
        user.addCreatedAction(action);

        location.addUser(user);
        locationRepository.save(location);
    }
}
