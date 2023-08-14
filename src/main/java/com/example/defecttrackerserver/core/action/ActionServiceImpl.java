package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService{

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;
    private final SecurityService securityService;
    private final UserMapper userMapper;

    @Override
    @Transactional
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
            List<Integer> assignedUserIds,
            List<Integer> defectIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            List<Integer> createdByIds,
            List<Integer> changedByIds,
            Pageable pageable
    ){
        Specification<Action> spec = Specification.where(null);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("description")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("id").as(String.class)), "%" + searchTerm.toLowerCase() + "%")
                    )
            );
        }

        if (dueDateStart != null && dueDateEnd != null) {
            LocalDateTime startOfDay = dueDateStart.atStartOfDay();
            LocalDateTime endOfDay = dueDateEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("dueDate"), startOfDay, endOfDay));
        }

        if(isCompleted != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isCompleted"), isCompleted));
        }

        if(assignedUserIds != null && !assignedUserIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("assignedUsers").get("id").in(assignedUserIds));
        }

        if(defectIds != null && !defectIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("defect").get("id").in(defectIds));
        }

        if (createdAtStart != null && createdAtEnd != null) {
            LocalDateTime startOfDay = createdAtStart.atStartOfDay();
            LocalDateTime endOfDay = createdAtEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), startOfDay, endOfDay));
        }

        if (changedAtStart != null && changedAtEnd != null) {
            LocalDateTime startOfDay = changedAtStart.atStartOfDay();
            LocalDateTime endOfDay = changedAtEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), startOfDay, endOfDay));
        }

        if(createdByIds != null && !createdByIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("createdBy").get("id").in(createdByIds));
        }

        if(changedByIds != null && !changedByIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("changedBy").get("id").in(changedByIds));
        }

        Page<Action> actions = actionRepository.findAll(spec, pageable);
        List<ActionDto> actionDtos = actions.stream().map(actionMapper::mapToDto).toList();

        List<Action> filteredActions = actionRepository.findAll(spec);

        return new PaginatedResponse<>(
                actionDtos,
                actions.getTotalPages(),
                (int) actions.getTotalElements(),
                actions.getNumber(),
                getActionFilterValues(filteredActions)
        );
    }

    @Override
    public ActionFilterValues getActionFilterValues(List<Action> actions){
        List<Integer> actionIds = actions.stream().map(Action::getId).toList();

        ActionFilterValues actionFilterValues = new ActionFilterValues();

        actionFilterValues.setDueDates(
                actionRepository.findDistinctDueDate(actionIds)
        );
        actionFilterValues.setIsCompleted(
                actionRepository.findDistinctIsCompleted(actionIds)
        );
        actionFilterValues.setAssignedUsers(
                actionRepository.findDistinctAssignedUsers(actionIds).stream()
                        .map(UserInfo::new)
                        .collect(Collectors.toSet())
        );
        actionFilterValues.setDefects(
                actionRepository.findDistinctDefect(actionIds)
        );
        actionFilterValues.setCreatedDates(
                actionRepository.findDistinctCreatedAt(actionIds).stream()
                        .map(LocalDateTime::toLocalDate)
                        .collect(Collectors.toSet())
        );
        actionFilterValues.setChangedDates(
                actionRepository.findDistinctChangedAt(actionIds).stream()
                        .map(LocalDateTime::toLocalDate)
                        .collect(Collectors.toSet())
        );
        actionFilterValues.setCreatedByUsers(
                actionRepository.findDistinctCreatedBy(actionIds).stream()
                        .map(UserInfo::new)
                        .collect(Collectors.toSet()));
        actionFilterValues.setChangedByUsers(
                actionRepository.findDistinctChangedBy(actionIds).stream()
                        .map(UserInfo::new)
                        .collect(Collectors.toSet()));

        return actionFilterValues;
    }

    @Override
    @Transactional
    public void closeAction(Integer actionId, Boolean isCompleted){
        Action actionToUpdate = actionRepository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));

        boolean isAuthorized = actionToUpdate.getAssignedUsers()
                .stream().anyMatch(user -> user.getUsername().equals(securityService.getUsername()));

        if(!isAuthorized && !securityService.hasRole("ROLE_ADMIN")){
            throw new AccessDeniedException("You are not authorized to close this action");
        }
        actionToUpdate.setChangedBy(securityService.getUser());
        actionToUpdate.setChangedAt(LocalDateTime.now());
        actionToUpdate.setIsCompleted(isCompleted);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAction(Integer id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));

        Defect defect = action.getDefect();
        defect.deleteAction(action);

        actionRepository.delete(action);
    }
}
