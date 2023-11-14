package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ActionMapperTest {
    @Mock
    private EntityService entityService;

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

        when(entityService.getDefectById(any(Integer.class))).thenReturn(defect);
        when(entityService.getUserById(any(Integer.class))).thenReturn(user);

        Action mappedAction = actionMapper.map(actionDto, new Action());

        assertEquals(actionDto.getDescription(), mappedAction.getDescription());
        assertEquals(actionDto.getIsCompleted(), mappedAction.getIsCompleted());
        assertEquals(actionDto.getCreatedAt(), mappedAction.getCreatedAt());
        assertEquals(actionDto.getDueDate(), mappedAction.getDueDate());
        assertEquals(actionDto.getCreatedBy().getId(), (mappedAction.getCreatedBy().getId()));
        assertEquals(actionDto.getAssignedUsers().size(), mappedAction.getAssignedUsers().size());
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

