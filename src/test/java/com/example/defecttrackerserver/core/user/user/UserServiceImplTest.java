package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.core.user.user.UserServiceImpl;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1);
        locationDto.setName("Texas");
        Location location = new Location();
        location.setId(1);
        location.setName("Texas");


        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("test");
        userDto.setPassword("test");
        userDto.setMail("test");
        userDto.setLocation(locationDto);

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
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        when(userMapper.map(any(UserDto.class))).thenReturn(user);

        UserDto result = userService.saveUser(userDto);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getMail(), result.getMail());
    }

    @Test
    void shouldReturnUserById(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

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
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

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
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        when(userMapper.map(any(UserDto.class))).thenReturn(user);

        UserDto result = userService.updateUser(userDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getMail(), result.getMail());
    }

    @Test
    void shouldDeleteUserById(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user)); //TODO: Does this make sense?

        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void ShouldReturnUserByUsername(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

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