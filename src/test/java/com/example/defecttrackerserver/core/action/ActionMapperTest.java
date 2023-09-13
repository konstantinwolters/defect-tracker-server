package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ActionMapperTest {
    @Mock
    private DefectRepository defectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ActionMapper actionMapper;

    ActionDto actionDto;
    UserDto userDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        userDto = TestHelper.setUpUserDto();
        actionDto = TestHelper.setUpActionDto();
        actionDto.setCreatedBy(userDto);
        actionDto.setChangedBy(userDto);
        actionDto.setAssignedUsers(new HashSet<>(Set.of(userDto)));
    }

    @Test
    void shouldReturnMappedAction() {
        User user = TestHelper.setUpUser();
        Defect defect = TestHelper.setUpDefect();

        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        Action mappedAction = actionMapper.map(actionDto, new Action());

        assertEquals(actionDto.getDescription(), mappedAction.getDescription());
        assertEquals(actionDto.getIsCompleted(), mappedAction.getIsCompleted());
        assertEquals(actionDto.getCreatedAt(), mappedAction.getCreatedAt());
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
    void shouldReturnMappedActionDto() {
        User user = TestHelper.setUpUser();
        Defect defect = TestHelper.setUpDefect();

        Action action = TestHelper.setUpAction();
        action.setCreatedBy(user);
        action.setChangedBy(user);
        action.setDefect(defect);

        when(userMapper.mapToDto(any(User.class))).thenReturn(userDto);

        ActionDto mappedActionDto = actionMapper.mapToDto(action);

        assertEquals(action.getId(), mappedActionDto.getId());
        assertEquals(action.getDescription(), mappedActionDto.getDescription());
        assertEquals(action.getIsCompleted(), mappedActionDto.getIsCompleted());
        assertEquals(action.getCreatedAt(), mappedActionDto.getCreatedAt());
        assertEquals(action.getChangedAt(), mappedActionDto.getChangedAt());
        assertEquals(action.getDueDate(), mappedActionDto.getDueDate());
        assertEquals(action.getDefect().getId(), mappedActionDto.getDefect());
        assertEquals(action.getCreatedBy().getId(), (mappedActionDto.getCreatedBy().getId()));
        assertEquals(action.getChangedBy().getId(), (mappedActionDto.getChangedBy().getId()));
        assertEquals(action.getAssignedUsers().size(), mappedActionDto.getAssignedUsers().size());
    }
}

