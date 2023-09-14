package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import com.example.defecttrackerserver.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
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

    @Mock
    private ActionSpecification actionSpecification;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Utils utils;

    @InjectMocks
    private ActionServiceImpl actionService;

    private ActionDto actionDto;
    private Action action;

    @BeforeEach
    void setUp() {

        User user = TestHelper.setUpUser();
        actionDto = TestHelper.setUpActionDto();
        action = TestHelper.setUpAction();
        action.setAssignedUsers(new HashSet<>(Set.of(user)));
    }

    @Test
    void shouldSaveAction() {
        when(actionRepository.save(action)).thenReturn(action);
        when(actionMapper.map(any(ActionDto.class), any(Action.class))).thenReturn(action);
        when(actionMapper.mapToDto(action)).thenReturn(actionDto);
        when(securityService.getUser()).thenReturn(new User());

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
    void shouldReturnFilteredActions() {
        String searchTerm = "test";
        LocalDate dueDateStart = LocalDate.now();
        LocalDate dueDateEnd = LocalDate.now();
        Boolean isComplete = true;
        String assignedUserIds = "1,2";
        String defectIds = "1,2";
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
        Page<Action> pageObject = new PageImpl<>(List.of(action));

        Specification<Action> spec = mock(Specification.class);

        when(actionSpecification.createSpecification(
                eq(searchTerm), eq(dueDateStart), eq(dueDateEnd), eq(isComplete),
                anyList(), anyList(), eq(createdAtStart), eq(createdAtEnd),
                eq(changedAtStart), eq(changedAtEnd), anyList(), anyList()
        )).thenReturn(spec);
        when(actionRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageObject);
        when(actionMapper.mapToDto(action)).thenReturn(actionDto);
        when(utils.convertStringToListOfInteger(any(String.class))).thenReturn(Arrays.asList(1,2));


        PaginatedResponse<ActionDto> result = actionService.getActions(searchTerm, dueDateStart, dueDateEnd, isComplete,
                assignedUserIds, defectIds, createdAtStart, createdAtEnd, changedAtStart, changedAtEnd,
                createdByIds, changedByIds, page, size, sort);

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(actionDto));
        assertEquals(pageObject.getTotalPages(), result.getTotalPages());
        assertEquals((int) pageObject.getTotalElements(),result.getTotalElements());
        assertEquals(pageObject.getNumber(), result.getCurrentPage());
    }

    @Test
    void shouldCloseAction(){
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(action));
        when(securityService.getUsername()).thenReturn("testUsername");

        actionService.closeAction(1);
        assertEquals(action.getIsCompleted(), true);
    }

    @Test
    void shouldThrowExceptionWhenNotAuthorizedToCloseAction(){
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(action));
        when(securityService.getUsername()).thenReturn("Bob");

        assertThrows(AccessDeniedException.class,
                () -> actionService.closeAction(1));
    }

    @Test
    void shouldUpdateAction() {
        when(actionRepository.save(any(Action.class))).thenReturn(action);
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(action));
        when(actionMapper.map(any(ActionDto.class), any(Action.class))).thenReturn(action);
        when(actionMapper.mapToDto(any(Action.class))).thenReturn(actionDto);

        ActionDto result = actionService.updateAction(1, actionDto);

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
