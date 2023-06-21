package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectDto;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import com.example.defecttrackerserver.core.user.user.userException.UserExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ActionMapperTest {

    @InjectMocks
    private ActionMapper actionMapper;

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private UserRepository userRepository;

    ActionDto actionDto;
    UserDto userDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        actionDto = new ActionDto();
        actionDto.setId(1);
        actionDto.setDescription("testDescription");
        actionDto.setDueDate(LocalDate.of(2023,1,1));
        actionDto.setIsCompleted(true);

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testName");

        actionDto.setAssignedUsers(Set.of(userDto));
        actionDto.setCreatedBy(userDto);

        actionDto.setDefect(1);
    }

    @Test
    void shouldReturnMappedAction() {
        User user = new User();
        user.setId(1);

        Defect defect = new Defect();
        defect.setId(1);

        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        Action action = new Action();
        Action mappedAction = actionMapper.map(actionDto, action);

        assertEquals(actionDto.getDescription(), mappedAction.getDescription());
        assertEquals(actionDto.getIsCompleted(), mappedAction.getIsCompleted());
        assertEquals(actionDto.getDueDate(), mappedAction.getDueDate());
        assertEquals(actionDto.getCreatedBy().getId(), (mappedAction.getCreatedBy().getId()));
        assertEquals(actionDto.getAssignedUsers().size(), mappedAction.getAssignedUsers().size());
    }

    @Test
    void shouldThrowExceptionWhenDefectNotFound() {
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> actionMapper.map(actionDto, new Action()));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
                when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Defect()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> actionMapper.map(actionDto, new Action()));
    }

    @Test
    void shouldThrowExceptionWhenNullOrEmptyFields() {
        actionDto.setDueDate(null);
        assertThrows(IllegalArgumentException.class, () -> actionMapper.checkNullOrEmptyFields(actionDto));

        actionDto.setDueDate(LocalDate.of(2023,1,1));
        actionDto.setDescription("");
        assertThrows(IllegalArgumentException.class, () -> actionMapper.checkNullOrEmptyFields(actionDto));

        actionDto.setDescription("test");
        actionDto.setAssignedUsers(null);
        assertThrows(IllegalArgumentException.class, () -> actionMapper.checkNullOrEmptyFields(actionDto));

        actionDto.setAssignedUsers(Set.of(new UserDto()));
        actionDto.setDefect(null);
        assertThrows(IllegalArgumentException.class, () -> actionMapper.checkNullOrEmptyFields(actionDto));

        actionDto.setDefect(1);
        actionDto.setCreatedBy(null);
        assertThrows(IllegalArgumentException.class, () -> actionMapper.checkNullOrEmptyFields(actionDto));
    }
}

