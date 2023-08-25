package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationMapper;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleMapper;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import com.example.defecttrackerserver.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectCommentRepository defectCommentRepository;

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSpecification userSpecification;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private LocationMapper locationMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private SecurityService securityService;

    @Mock
    private Utils utils;

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
        when(securityService.getUser()).thenReturn(user);

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
    void shouldReturnFilteredUsers() {
        String searchTerm = "test";
        Boolean isActive = true;
        String locationIds = "1,2";
        String roleIds = "1,2";
        LocalDate createdAtStart = LocalDate.now();
        LocalDate createdAtEnd = LocalDate.now();
        LocalDate changedAtStart = LocalDate.now();
        LocalDate changedAtEnd = LocalDate.now();
        Integer page = 0;
        Integer size = 10;
        String createdByIds = "1,2";
        String changedByIds = "1,2";
        String sort = "id,desc";
        Pageable pageable = PageRequest.of(0,10, Sort.Direction.DESC, "id");
        Page<User> pageObject = new PageImpl<>(List.of(user));

        Specification<User> spec = mock(Specification.class);

        when(userSpecification.createSpecification(
                eq(searchTerm), eq(isActive),
                anyList(), anyList(), eq(createdAtStart), eq(createdAtEnd),
                eq(changedAtStart), eq(changedAtEnd), anyList(), anyList()
        )).thenReturn(spec);
        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageObject);
        when(userMapper.mapToDto(user)).thenReturn(userDto);
        when(utils.convertStringToListOfInteger(any(String.class))).thenReturn(Arrays.asList(1,2));


        PaginatedResponse<UserDto> result = userService.getUsers(searchTerm, isActive,
                locationIds, roleIds, createdAtStart, createdAtEnd, changedAtStart, changedAtEnd,
                createdByIds, changedByIds, page, size, sort);

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(userDto));
        assertEquals(pageObject.getTotalPages(), result.getTotalPages());
        assertEquals((int) pageObject.getTotalElements(),result.getTotalElements());
        assertEquals(pageObject.getNumber(), result.getCurrentPage());
    }


    @Test
    void shouldUpdateUser(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(securityService.getUsername()).thenReturn("test");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.mapToDto(user)).thenReturn(userDto);
        when(userMapper.mapToEntity(any(UserDto.class), any(User.class))).thenReturn(user);
        when(securityService.getUser()).thenReturn(user);

        UserDto result = userService.updateUser(1, userDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getMail(), result.getMail());
    }

    @Test
    void shouldDeleteUserById(){
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(defectRepository.findByChangedById(anyInt())).thenReturn(Set.of());
        when(defectRepository.findByCreatedById(anyInt())).thenReturn(Set.of());
        when(defectCommentRepository.findByCreatedById(anyInt())).thenReturn(Set.of());
        when(actionRepository.findByChangedById(anyInt())).thenReturn(Set.of());
        when(actionRepository.findByCreatedById(anyInt())).thenReturn(Set.of());
        when(actionRepository.findByAssignedUsersId(anyInt())).thenReturn(Set.of());
        when(userRepository.findByChangedById(anyInt())).thenReturn(Set.of());
        when(userRepository.findByChangedById(anyInt())).thenReturn(Set.of());

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