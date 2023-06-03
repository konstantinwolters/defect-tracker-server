package com.example.defecttrackerserver.core.user;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenSavedUserWithDuplicateUsername_thenThrowException() {
        // given
        User user1 = new User();
        user1.setUsername("testUser");

        User user2 = new User();
        user2.setUsername("testUser");

        // when
        userRepository.saveAndFlush(user1);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }
}

