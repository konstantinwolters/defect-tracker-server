package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityService securityService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        Location location = new Location();
        location.setId(1);
        location.setName("Texas");

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("test");
        userDto.setPassword("test");
        userDto.setMail("test");
        userDto.setLocation("Texas");

        user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("test");
        user.setMail("test");
        user.setLocation(location);
    }

    @Test
    void shouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role()));
        when(userMapper.mapToDto(user)).thenReturn(userDto);
        when(passwordEncoder.encode(any())).thenReturn("test");
        when(userMapper.mapToEntity(any(UserDto.class), any(User.class))).thenReturn(user);

        UserDto result = userService.saveUser(userDto);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getMail(), result.getMail());
    }

    @Test
    void shouldReturnUserById(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userMapper.mapToDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getMail(), result.getMail());
    }
    @Test
    void shouldReturnAllUsers(){
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.mapToDto(user)).thenReturn(userDto);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(user.getId(), result.get(0).getId());
        assertEquals(user.getUsername(), result.get(0).getUsername());
        assertEquals(user.getPassword(), result.get(0).getPassword());
        assertEquals(user.getMail(), result.get(0).getMail());
    }

    @Test
    void shouldUpdateUser(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(securityService.getUsername()).thenReturn("test");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.mapToDto(user)).thenReturn(userDto);
        when(userMapper.mapToEntity(any(UserDto.class), any(User.class))).thenReturn(user);

        UserDto result = userService.updateUser(1, userDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getMail(), result.getMail());
    }

    @Test
    void shouldDeleteUserById(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void ShouldReturnUserByUsername(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userMapper.mapToDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserByUsername("test");

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getMail(), result.getMail());
    }

    @Test
    void ShouldThrowExceptionWhenUserIdNotFound(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void ShouldThrowExceptionWhenUsernameNotFound() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername("testuser"));
    }
}