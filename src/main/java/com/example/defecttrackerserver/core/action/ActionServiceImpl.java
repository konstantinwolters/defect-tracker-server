package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.auth.authException.UnauthorizedAccessException;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService{

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;
    private SecurityService securityService;

    @Override
    public ActionDto saveAction(ActionDto actionDto) {
        actionDto.setId(null);
        actionDto.setCreatedOn(LocalDateTime.now());
        Action action = new Action();
        Action newAction = actionMapper.map(actionDto, action);
        newAction.setIsCompleted(false);
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
    public List<ActionDto> getAllActions() {
        List<Action> actions = actionRepository.findAll();
        return actions.stream().map(actionMapper::mapToDto).toList();
    }

    @Override
    public List<ActionDto> getAllActionsByUserCreatedId(Integer userId) {
        List<Action> actions = actionRepository.findByCreatedBy_Id(userId);
        return actions.stream()
                .map(actionMapper::mapToDto).toList();
    }

    @Override
    public List<ActionDto> getFilteredActions(
            String dueDateStart,
            String dueDateEnd,
            Boolean isCompleted,
            List<Integer> assignedUserIds,
            List<Integer> defectIds,
            String createdOnStart,
            String createdOnEnd,
            List<Integer> createdByIds
    ){
        Specification<Action> spec = Specification.where(null);

        if(dueDateStart != null && dueDateEnd != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateObj = LocalDate.parse(dueDateStart, formatter);
            LocalDate endDateObj = LocalDate.parse(dueDateEnd, formatter);
            LocalDateTime startOfDay = startDateObj.atStartOfDay();
            LocalDateTime endOfDay = endDateObj.atStartOfDay().plusDays(1).minusSeconds(1);

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

        if(createdOnStart != null && createdOnEnd != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateObj = LocalDate.parse(createdOnStart, formatter);
            LocalDate endDateObj = LocalDate.parse(createdOnEnd, formatter);
            LocalDateTime startOfDay = startDateObj.atStartOfDay();
            LocalDateTime endOfDay = endDateObj.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("createdOn"), startOfDay, endOfDay));
        }

        if(createdByIds != null && !createdByIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("createdBy").get("id").in(createdByIds));
        }
        return actionRepository.findAll(spec).stream().map(actionMapper::mapToDto).toList();
    }

    @Override
    @Transactional
    public void closeAction(Integer actionId, Boolean isCompleted){
        Action actionToUpdate = actionRepository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));

        boolean isAuthorized = actionToUpdate.getAssignedUsers()
                .stream().anyMatch(user -> user.getUsername().equals(securityService.getUsername()));

        if(!isAuthorized){
            throw new UnauthorizedAccessException("Unauthorized access.");
        }
        actionToUpdate.setIsCompleted(isCompleted);
    }

    @Override
    @Transactional
    public ActionDto updateAction(ActionDto actionDto) {
        Action actionToUpdate = actionRepository.findById(actionDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionDto.getId()));

        Action mappedAction = actionMapper.map(actionDto, actionToUpdate);

        Action updatedAction = actionRepository.save(mappedAction);
        return actionMapper.mapToDto(updatedAction);
    }

    @Override
    @Transactional
    public void deleteAction(Integer id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));

        Defect defect = action.getDefect();
        defect.deleteAction(action);

        actionRepository.delete(action);
    }
}
