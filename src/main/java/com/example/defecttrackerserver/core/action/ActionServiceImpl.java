package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import com.example.defecttrackerserver.notification.NotifyUsers;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import com.example.defecttrackerserver.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService{

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;
    private final ActionSpecification actionSpecification;
    private final SecurityService securityService;
    private final UserMapper userMapper;
    private final Utils utils;

    @Override
    @Transactional
    @NotifyUsers
    @PreAuthorize("hasRole('ROLE_QA') or hasRole('ROLE_ADMIN')")
    public ActionDto saveAction(ActionDto actionDto) {
        actionDto.setId(null);
        actionDto.setCreatedAt(LocalDateTime.now());

        User currentUser = securityService.getUser();
        actionDto.setCreatedBy(userMapper.mapToDto(currentUser));
        actionDto.setIsCompleted(false);

        Action newAction = actionMapper.map(actionDto, new Action());
        Action savedAction = actionRepository.save(newAction);
        return actionMapper.mapToDto(savedAction);
    }

    @Override
    public ActionDto getActionById(Integer id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));
        return actionMapper.mapToDto(action);
    }

    @Override
    public PaginatedResponse<ActionDto> getActions(
            String searchTerm,
            LocalDate dueDateStart,
            LocalDate dueDateEnd,
            Boolean isCompleted,
            String assignedUserIds,
            String defectIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            String createdByIds,
            String changedByIds,
            Integer page,
            Integer size,
            String sort
    ){
        List<Integer> assignedUserIdList = utils.convertStringToListOfInteger(assignedUserIds);
        List<Integer> defectIdList = utils.convertStringToListOfInteger(defectIds);
        List<Integer> createdByIdList = utils.convertStringToListOfInteger(createdByIds);
        List<Integer> changedByIdList = utils.convertStringToListOfInteger(changedByIds);

        Sort sorting = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] split = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(split[1]);
            sorting = Sort.by(direction, split[0]);
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        Specification<Action> spec = actionSpecification.createSpecification(
                searchTerm,
                dueDateStart,
                dueDateEnd,
                isCompleted,
                assignedUserIdList,
                defectIdList,
                createdAtStart,
                createdAtEnd,
                changedAtStart,
                changedAtEnd,
                createdByIdList,
                changedByIdList
        );

        Page<Action> actions = actionRepository.findAll(spec, pageable);
        List<ActionDto> actionDtos = actions.stream().map(actionMapper::mapToDto).toList();

        List<Action> filteredActions = actionRepository.findAll(spec);

        return new PaginatedResponse<>(
                actionDtos,
                actions.getTotalPages(),
                (int) actions.getTotalElements(),
                actions.getNumber(),
                getActionFilterValues(filteredActions) // provide distinct filter values for Actions meeting the filter criteria
        );
    }

    //Returns distinct filter values for Actions meeting the filter criteria
    @Override
    public ActionFilterValues getActionFilterValues(List<Action> actions) {
        List<Integer> actionIds = actions.stream().map(Action::getId).toList();

        ActionFilterValues actionFilterValues = new ActionFilterValues();
        actionFilterValues.setDueDates(actionRepository.findDistinctDueDate(actionIds));
        actionFilterValues.setIsCompleted(actionRepository.findDistinctIsCompleted(actionIds));
        actionFilterValues.setAssignedUsers(utils.mapToSet(
                actionRepository.findDistinctAssignedUsers(actionIds), UserInfo::new));

        actionFilterValues.setDefects(actionRepository.findDistinctDefect(actionIds));
        actionFilterValues.setCreatedDates(utils.mapToSet(
                actionRepository.findDistinctCreatedAt(actionIds), LocalDateTime::toLocalDate));

        actionFilterValues.setChangedDates(utils.mapToSet(
                actionRepository.findDistinctChangedAt(actionIds), LocalDateTime::toLocalDate));

        actionFilterValues.setCreatedByUsers(utils.mapToSet(
                actionRepository.findDistinctCreatedBy(actionIds), UserInfo::new));

        actionFilterValues.setChangedByUsers(utils.mapToSet(
                actionRepository.findDistinctChangedBy(actionIds), UserInfo::new));

        return actionFilterValues;
    }

    @Override
    @Transactional
    public void closeAction(Integer actionId){
        Action actionToUpdate = actionRepository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));

        boolean isAuthorized = actionToUpdate.getAssignedUsers()
                .stream().anyMatch(user -> user.getUsername().equals(securityService.getUsername()));

        if(!isAuthorized && !securityService.hasRole("ROLE_ADMIN")){
            throw new AccessDeniedException("You are not authorized to close this action");
        }
        actionToUpdate.setChangedBy(securityService.getUser());
        actionToUpdate.setChangedAt(LocalDateTime.now());
        actionToUpdate.setIsCompleted(true);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_QA') or hasRole('ROLE_ADMIN')")
    public ActionDto updateAction(Integer actionId, ActionDto actionDto) {
        Action actionToUpdate = actionRepository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));
        actionToUpdate.setChangedBy(securityService.getUser());
        actionToUpdate.setChangedAt(LocalDateTime.now());
        Action mappedAction = actionMapper.map(actionDto, actionToUpdate);
        Action updatedAction = actionRepository.save(mappedAction);
        return actionMapper.mapToDto(updatedAction);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_QA') or hasRole('ROLE_ADMIN')")
    public void deleteAction(Integer id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));

        Defect defect = action.getDefect();
        defect.deleteAction(action);

        actionRepository.delete(action);
    }
}
