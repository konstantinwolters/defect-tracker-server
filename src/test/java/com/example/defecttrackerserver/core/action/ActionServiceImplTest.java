package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.auth.authException.UnauthorizedAccessException;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserDto;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActionServiceImplTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private SecurityService securityService;

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
        action.setDefect(new Defect());
        action.setDescription("test");

        User user = new User();
        user.setUsername("testUser");
        action.setAssignedUsers(new HashSet<>(Set.of(user)));
    }

    @Test
    void shouldSaveAction() {
        when(actionRepository.save(action)).thenReturn(action);
        when(actionMapper.map(any(ActionDto.class), any(Action.class))).thenReturn(action);
        when(actionMapper.mapToDto(action)).thenReturn(actionDto);

        ActionDto result = actionService.saveAction(actionDto);

        assertNotNull(result);
        assertEquals(action.getDescription(), result.getDescription());
        verify(actionRepository, times(1)).save(action);
    }

    @Test
    void shouldReturnActionById() {
        when(actionRepository.findById(1)).thenReturn(Optional.ofNullable(action));
        when(actionMapper.mapToDto(action)).thenReturn(actionDto);

        ActionDto result = actionService.getActionById(1);

        assertNotNull(result);
        assertEquals(action.getId(), result.getId());
        assertEquals(action.getDescription(), result.getDescription());
    }

    @Test
    void shouldReturnAllActions() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Action> page = new PageImpl<>(List.of(action));

        when(actionRepository.findAll(pageable)).thenReturn(page);
        when(actionMapper.mapToDto(action)).thenReturn(actionDto);

        PaginatedResponse<ActionDto> result = actionService.getAllActions(pageable);

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(actionDto));
        assertEquals(page.getTotalPages(), result.getTotalPages());
        assertEquals((int) page.getTotalElements(),result.getTotalElements());
        assertEquals(page.getNumber(), result.getCurrentPage());
    }

    @Test
    void shouldReturnFilteredActions() {
        String dueDateStart = "2023-01-01";
        String dueDateEnd = "2023-01-31";
        Boolean isComplete = true;
        List<Integer> assignedUserIds = Arrays.asList(1,2);
        List<Integer> defectIds = Arrays.asList(1,2);
        String createdOnStart = "2023-01-01";
        String createdOnEnd = "2023-01-31";;
        List<Integer> createdByIds = Arrays.asList(1,2);
        Pageable pageable = PageRequest.of(0,10);
        Page<Action> page = new PageImpl<>(List.of(action));

        when(actionRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(actionMapper.mapToDto(action)).thenReturn(actionDto);

        PaginatedResponse<ActionDto> result = actionService.getFilteredActions(dueDateStart, dueDateEnd, isComplete,
                assignedUserIds, defectIds, createdOnStart, createdOnEnd, createdByIds, pageable);

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(actionDto));
        assertEquals(page.getTotalPages(), result.getTotalPages());
        assertEquals((int) page.getTotalElements(),result.getTotalElements());
        assertEquals(page.getNumber(), result.getCurrentPage());
    }

    @Test
    void shouldCloseAction(){
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(action));
        when(securityService.getUsername()).thenReturn("testUser");

        actionService.closeAction(1, true);
        assertEquals(action.getIsCompleted(), true);
    }

    @Test
    void shouldThrowExceptionWhenNotAuthorizedToCloseAction(){
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(action));
        when(securityService.getUsername()).thenReturn("Bob");

        assertThrows(UnauthorizedAccessException.class,
                () -> actionService.closeAction(1, true));
    }
    @Test
    void shouldUpdateAction() {
        when(actionRepository.save(any(Action.class))).thenReturn(action);
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(action));
        when(actionMapper.map(any(ActionDto.class), any(Action.class))).thenReturn(action);
        when(actionMapper.mapToDto(any(Action.class))).thenReturn(actionDto);

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
