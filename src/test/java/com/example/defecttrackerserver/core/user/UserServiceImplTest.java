package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.user.role.Role;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("test");
        userDto.setPassword("test");
        userDto.setMail("test");

        user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("test");
        user.setMail("test");
    }

    @Test
    void shouldSaveUser() {
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.saveUser(userDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getMail(), result.getMail());
        verify(userRepository, times(1)).save(user);
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
        verify(userRepository, times(1)).findById(anyInt());
    }
    @Test
    void shouldReturnAllUsers(){
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        List<UserDto> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(user.getId(), result.get(0).getId());
        assertEquals(user.getUsername(), result.get(0).getUsername());
        assertEquals(user.getPassword(), result.get(0).getPassword());
        assertEquals(user.getMail(), result.get(0).getMail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateUser(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.updateUser( userDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getMail(), result.getMail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldDeleteUserById(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).findById(anyInt());
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
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void shouldReturnRolesFromUserById(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        Set<Role> result = userService.getRoles(1);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    void ShouldThrowExceptionWhenUserIdNotFound(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getUserById(1));
    }

    @Test
    void ShouldThrowExceptionWhenUsernameNotFound() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getUserByUsername("testuser"));
    }
}