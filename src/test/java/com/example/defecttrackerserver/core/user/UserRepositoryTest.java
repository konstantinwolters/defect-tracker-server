package com.example.defecttrackerserver.core.user;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    User user1;
    User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user2 = new User();
    }

    @Test
    public void whenSavedUserWithDuplicateUsername_thenThrowException() {
        user1.setUsername("testUser");
        user1.setMail("testUser@test.de");
        user1.setPassword("XXXXXXXXXXXX");
        userRepository.saveAndFlush(user1);

        user2.setUsername("testUser");
        user2.setPassword("XXXXXXXXXXXX");
        user2.setMail("testUser@test.de");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }

    @Test
    public void whenSavedUserWithNoUsername_thenThrowException() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user1);
        });
    }

    @Test
    public void whenSavedUserWithDuplicateMail_thenThrowException() {
        user1.setUsername("testUser");
        user1.setPassword("XXXXXXXXXXXX");
        user1.setMail("testUser@test.de");
        userRepository.saveAndFlush(user1);

        user2.setUsername("testUser");
        user2.setPassword("XXXXXXXXXXXX");
        user2.setMail("testUser@test.de");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }

    @Test
    public void whenSavedUserWithNoMail_thenThrowException() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user1);
        });
    }
}

