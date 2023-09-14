package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

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

        locationDto = TestHelper.setUpLocationDto();
        userDto = TestHelper.setUpUserDto();
    }

    @Test
    void shouldReturnMappedUser() {
        Location locationStub = TestHelper.setUpLocation();

        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(locationStub));
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(new Role()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));

        User mappedUser = userMapper.mapToEntity(userDto, new User());

        assertEquals(userDto.getUsername(), mappedUser.getUsername());
        assertEquals(userDto.getPassword(), mappedUser.getPassword());
        assertEquals(userDto.getMail(), mappedUser.getMail());
        assertEquals(userDto.getFirstName(), mappedUser.getFirstName());
        assertEquals(userDto.getLastName(), mappedUser.getLastName());
        assertEquals(userDto.getIsActive(), mappedUser.getIsActive());
        assertEquals(userDto.getChangedAt(), mappedUser.getChangedAt());
        assertEquals(userDto.getCreatedAt(), mappedUser.getCreatedAt());
        assertEquals(userDto.getCreatedBy(), mappedUser.getCreatedById());
        assertEquals(userDto.getRoles().size(), mappedUser.getRoles().size());
        assertEquals(userDto.getAssignedActions().size(), mappedUser.getAssignedActions().size());
    }

    @Test
    void shouldHandleNullRoles() {
        userDto.setRoles(null);

        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));

        User mappedUser = userMapper.mapToEntity(userDto, new User());

        assertNotNull(mappedUser.getRoles());
        assertTrue(mappedUser.getRoles().isEmpty());
    }

    @Test
    void shouldHandleAssignedActions() {
        userDto.setAssignedActions(null);

        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(new Role()));

        User mappedUser = userMapper.mapToEntity(userDto, new User());

        assertNotNull(mappedUser.getAssignedActions());
        assertTrue(mappedUser.getAssignedActions().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFound() {
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userMapper.mapToEntity(userDto, new User()));
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userMapper.mapToEntity(userDto, new User()));
    }
    @Test
    void shouldThrowExceptionWhenActionNotFound() {
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(new Role()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userMapper.mapToEntity(userDto, new User()));
    }

    @Test
    void shouldThrowExceptionWhenDuplicateUserEntriesOnSave() {
        User duplicateUser = TestHelper.setUpUser();

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(duplicateUser));
        assertThrows(DuplicateKeyException.class, () -> userMapper.checkDuplicateUserEntries(userDto));

        when(userRepository.findByMail(any())).thenReturn(Optional.of(new User()));
        assertThrows(DuplicateKeyException.class, () -> userMapper.checkDuplicateUserEntries(userDto));
    }

    @Test
    void shouldThrowExceptionWhenDuplicateUserEntriesOnUpdate() {
        User existingUser = TestHelper.setUpUser();
        User duplicateUser = TestHelper.setUpUser();
        duplicateUser.setId(2);

        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByMail(any())).thenReturn(Optional.of(duplicateUser));

        assertThrows(DuplicateKeyException.class, () -> userMapper.checkDuplicateUserEntries(userDto));
    }

    @Test
    void shouldReturnMappedUserDto() {
        User user = TestHelper.setUpUser();

        UserDto mappedUserDto = userMapper.mapToDto(user);

        assertEquals(user.getId(), mappedUserDto.getId());
        assertEquals(user.getUsername(), mappedUserDto.getUsername());
        assertEquals(user.getMail(), mappedUserDto.getMail());
        assertNull(mappedUserDto.getPassword());
        assertEquals(user.getFirstName(), mappedUserDto.getFirstName());
        assertEquals(user.getLastName(), mappedUserDto.getLastName());
        assertEquals(user.getIsActive(), mappedUserDto.getIsActive());
        assertEquals(user.getChangedAt(), mappedUserDto.getChangedAt());
        assertEquals(user.getCreatedAt(), mappedUserDto.getCreatedAt());
        assertEquals(user.getCreatedById(), mappedUserDto.getCreatedBy());
        assertEquals(user.getRoles().size(), mappedUserDto.getRoles().size());
        assertEquals(user.getAssignedActions().size(), mappedUserDto.getAssignedActions().size());
        assertEquals(user.getLocation().getName(), mappedUserDto.getLocation());
    }
}

