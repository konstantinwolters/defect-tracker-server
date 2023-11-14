package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
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

    @Mock
    private EntityService entityService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserMapper userMapper;

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

        when(entityService.getLocationByName(any(String.class))).thenReturn(locationStub);
        when(entityService.getRoleByName(any(String.class))).thenReturn(new Role());
        when(entityService.getActionById(any(Integer.class))).thenReturn(new Action());

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
    void shouldThrowExceptionWhenDuplicateUserEntriesOnSave() {
        User duplicateUser = TestHelper.setUpUser();

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(duplicateUser));
        assertThrows(DuplicateKeyException.class, () -> userMapper.checkDuplicateUserEntries(userDto));

        when(userRepository.findByMail(any())).thenReturn(Optional.of(duplicateUser));
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

