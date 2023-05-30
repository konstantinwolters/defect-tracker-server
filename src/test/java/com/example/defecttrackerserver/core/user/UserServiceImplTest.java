package com.example.defecttrackerserver.core.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto testUser;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        testUser = new UserDto(user);
    }

    @Test
    void saveUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        userService.saveUser(testUser);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void getUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        userService.getUser(1);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));
        userService.getUsers();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("updateduser");
        userService.updateUser(1, updatedUser);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(any(User.class));
        userService.deleteUser(1);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void findByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        userService.findByUsername("testuser");
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void getRoles() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        userService.getUser(1);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getUserWhenNoSuchElement() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getUser(1));
    }

    @Test
    void updateUserWhenNoSuchElement() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.updateUser(1, new User()));
    }

    @Test
    void deleteUserWhenNoSuchElement() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(1));
    }

    @Test
    void findByUsernameWhenNoSuchElement() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.findByUsername("testuser"));
    }

    @Test
    void getRolesWhenNoSuchElement() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getRoles(1));
    }
}