package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.userException.UserExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private UserRepository userRepository;

    UserDto userDto;
    LocationDto locationDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("timtest");
        userDto.setPassword("Testermann");
        userDto.setMail("tim@testermann.de");
        userDto.setFirstName("Tim");
        userDto.setLastName("Testermann");
        locationDto = new LocationDto();
        locationDto.setId(1);
        userDto.setLocation("Texas");
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        userDto.getRoles().add("ROLE_ADMIN");
        ActionDto actionDto = new ActionDto();
        actionDto.setId(1);
        userDto.getAssignedActions().add(1);
    }

    @Test
    void shouldReturnMappedUser() {
        Location locationStub = new Location();
        locationStub.setId(locationDto.getId());

        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(locationStub));
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(new Role()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));

        User user = new User();
        User mappedUser = userMapper.map(userDto, user);

        assertEquals(userDto.getUsername(), mappedUser.getUsername());
        assertEquals(userDto.getPassword(), mappedUser.getPassword());
        assertEquals(userDto.getMail(), mappedUser.getMail());
        assertEquals(userDto.getFirstName(), mappedUser.getFirstName());
        assertEquals(userDto.getLastName(), mappedUser.getLastName());
        assertEquals(userDto.getRoles().size(), mappedUser.getRoles().size());
        assertEquals(userDto.getAssignedActions().size(), mappedUser.getAssignedActions().size());
    }

    @Test
    void shouldHandleNullRoles() {
        userDto.setRoles(null);

        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));

        User user = new User();
        User mappedUser = userMapper.map(userDto, user);

        assertNotNull(mappedUser.getRoles());
        assertTrue(mappedUser.getRoles().isEmpty());
    }

    @Test
    void shouldHandleAssignedActions() {
        userDto.setAssignedActions(null);

        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(new Role()));

        User user = new User();
        User mappedUser = userMapper.map(userDto, user);

        assertNotNull(mappedUser.getAssignedActions());
        assertTrue(mappedUser.getAssignedActions().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFound() {
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        User user = new User();
        assertThrows(EntityNotFoundException.class, () -> userMapper.map(userDto, user));
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        User user = new User();
        assertThrows(EntityNotFoundException.class, () -> userMapper.map(userDto, user));
    }
    @Test
    void shouldThrowExceptionWhenActionNotFound() {
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(new Role()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        User user = new User();
        assertThrows(EntityNotFoundException.class, () -> userMapper.map(userDto, user));
    }

    @Test
    void shouldThrowExceptionWhenNullOrEmptyFields() {
        UserDto userDto = new UserDto();
        userDto.setUsername("");
        assertThrows(IllegalArgumentException.class, () -> userMapper.checkNullOrEmptyFields(userDto));

        userDto.setUsername("Username");
        userDto.setMail(null);
        assertThrows(IllegalArgumentException.class, () -> userMapper.checkNullOrEmptyFields(userDto));

        userDto.setMail("XXXXXXXXXXXX");
        userDto.setPassword("");
        assertThrows(IllegalArgumentException.class, () -> userMapper.checkNullOrEmptyFields(userDto));

        userDto.setPassword("XXXXXXXXXXXX");
        userDto.setLocation(null);
        assertThrows(IllegalArgumentException.class, () -> userMapper.checkNullOrEmptyFields(userDto));
    }

    @Test
    void shouldThrowExceptionWhenDuplicateUserEntriesOnSave() {
        User duplicateUser = new User();
        duplicateUser.setId(1);

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(duplicateUser));
        assertThrows(UserExistsException.class, () -> userMapper.checkDuplicateUserEntries(userDto));

        when(userRepository.findByMail(any())).thenReturn(Optional.of(new User()));
        assertThrows(UserExistsException.class, () -> userMapper.checkDuplicateUserEntries(userDto));
    }

    @Test
    void shouldThrowExceptionWhenDuplicateUserEntriesOnUpdate() {
        User existingUser = new User();
        existingUser.setId(1);
        User duplicateUser = new User();
        duplicateUser.setId(2);

        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByMail(any())).thenReturn(Optional.of(duplicateUser));

        assertThrows(UserExistsException.class, () -> userMapper.checkDuplicateUserEntries(userDto));
    }
}

