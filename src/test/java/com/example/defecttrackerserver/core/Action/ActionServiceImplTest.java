package com.example.defecttrackerserver.core.Action;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.action.ActionServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActionServiceImplTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ActionServiceImpl actionService;

    private ActionDto actionDto;
    private Action action;

    @BeforeEach
    void setUp() {

        actionDto = new ActionDto();
        actionDto.setId(1);
        actionDto.setDescription("test");

        action = new Action();
        action.setId(1);
        action.setDescription("test");
    }

    @Test
    void shouldSaveUser() {
        when(actionRepository.save(action)).thenReturn(action);
        when(modelMapper.map(actionDto, Action.class)).thenReturn(action);
        when(modelMapper.map(action, ActionDto.class)).thenReturn(actionDto);

        ActionDto result = actionService.saveAction(actionDto);

        assertNotNull(result);
        assertEquals(action.getId(), result.getId());
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
}
