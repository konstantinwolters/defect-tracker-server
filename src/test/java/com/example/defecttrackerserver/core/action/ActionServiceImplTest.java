package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActionServiceImplTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ActionMapper actionMapper;

    @InjectMocks
    private ActionServiceImpl actionService;

    private ActionDto actionDto;
    private Action action;

    @BeforeEach
    void setUp() {
        actionDto = new ActionDto();
        actionDto.setId(1);
        actionDto.setAssignedUsers(Set.of(new UserDto()));
        actionDto.setDescription("test");
        actionDto.setCreatedBy(new UserDto());

        action = new Action();
        action.setId(1);
        action.setDescription("test");
    }

    @Test
    void shouldSaveAction() {
        when(actionRepository.save(action)).thenReturn(action);
        when(actionMapper.map(any(ActionDto.class), any(Action.class))).thenReturn(action);
        when(modelMapper.map(action, ActionDto.class)).thenReturn(actionDto);

        ActionDto result = actionService.saveAction(actionDto);

        assertNotNull(result);
        assertEquals(action.getDescription(), result.getDescription());
        verify(actionRepository, times(1)).save(action);
    }

    @Test
    void shouldReturnActionById() {
        when(actionRepository.findById(1)).thenReturn(Optional.ofNullable(action));
        when(modelMapper.map(action, ActionDto.class)).thenReturn(actionDto);

        ActionDto result = actionService.getActionById(1);

        assertNotNull(result);
        assertEquals(action.getId(), result.getId());
        assertEquals(action.getDescription(), result.getDescription());
    }

    @Test
    void shouldReturnAllActions() {
        when(actionRepository.findAll()).thenReturn(Arrays.asList(action));
        when(modelMapper.map(action, ActionDto.class)).thenReturn(actionDto);

        List<ActionDto> result = actionService.getAllActions();

        assertNotNull(result);
        assertEquals(action.getId(), result.get(0).getId());
        assertEquals(action.getDescription(), result.get(0).getDescription());
    }

    @Test
    void shouldReturnAllActionsByCreatedById() {
        when(actionRepository.findByCreatedBy_Id(1)).thenReturn(Arrays.asList(action));
        when(modelMapper.map(action, ActionDto.class)).thenReturn(actionDto);

        List<ActionDto> result = actionService.getAllActionsByUserCreatedId(1);

        assertNotNull(result);
        assertEquals(action.getId(), result.get(0).getId());
        assertEquals(action.getDescription(), result.get(0).getDescription());
    }


    @Test
    void shouldUpdateAction() {
        when(actionRepository.save(any(Action.class))).thenReturn(action);
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(action));
        when(actionMapper.map(any(ActionDto.class), any(Action.class))).thenReturn(action);
        when(modelMapper.map(any(Action.class), eq(ActionDto.class))).thenReturn(actionDto);

        ActionDto result = actionService.updateAction(actionDto);

        assertNotNull(result);
        assertEquals(action.getId(), result.getId());
        assertEquals(action.getDescription(), result.getDescription());
        verify(actionRepository, times(1)).save(action);
    }

    @Test
    void shouldDeleteAction() {
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(action));

        actionService.deleteAction(1);

        verify(actionRepository, times(1)).delete(action);
    }
}
