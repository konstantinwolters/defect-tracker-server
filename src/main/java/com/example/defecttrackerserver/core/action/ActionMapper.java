package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper that provides methods for mapping entity objects to DTOs and vice versa.
 */
@Component
@RequiredArgsConstructor
public class ActionMapper {
    private final EntityService entityService;
    private final UserMapper userMapper;

    private final Map<Integer, User> userCache = new HashMap<>();

    public Action map(ActionDto actionDto, Action action){
        Defect defect = actionDto.getDefect() != null ? entityService.getDefectById(actionDto.getDefect()): null;
        User createdBy = getUserById(actionDto.getCreatedBy().getId());
        User changedBy = actionDto.getChangedBy() != null ? getUserById(actionDto.getChangedBy().getId()) : null;

        Set<User> assignedUsers = actionDto.getAssignedUsers().stream()
                .map(userDto -> getUserById(userDto.getId()))
                .collect(Collectors.toSet());

        action.setDescription(actionDto.getDescription());
        action.setDueDate(actionDto.getDueDate());
        action.setIsCompleted(actionDto.getIsCompleted());
        action.setCreatedAt(actionDto.getCreatedAt());
        action.setChangedAt(actionDto.getChangedAt());
        action.setCreatedBy(createdBy);
        action.setChangedBy(changedBy);
        action.setDefect(defect);
        action.setAssignedUsers(assignedUsers);

        //Set up relationships
        if (defect != null)
            defect.addAction(action);

        assignedUsers.forEach(user -> user.addAssignedAction(action));

        return action;
    }

    public ActionDto mapToDto(Action action){
        Set<UserDto> assignedUsers = action.getAssignedUsers().stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toSet());

        Integer defectId = action.getDefect() != null ? action.getDefect().getId() : null;
        UserDto changedBy = action.getChangedBy() != null ? userMapper.mapToDto(action.getChangedBy()) : null;

        ActionDto actionDto = new ActionDto();
        actionDto.setId(action.getId());
        actionDto.setDescription(action.getDescription());
        actionDto.setIsCompleted(action.getIsCompleted());
        actionDto.setDueDate(action.getDueDate());
        actionDto.setAssignedUsers(assignedUsers);
        actionDto.setDefect(defectId);
        actionDto.setCreatedAt(action.getCreatedAt());
        actionDto.setChangedAt(action.getChangedAt());
        actionDto.setCreatedBy(userMapper.mapToDto(action.getCreatedBy()));
        actionDto.setChangedBy(changedBy);

        return actionDto;
    }

    private User getUserById(Integer id){
        return userCache.computeIfAbsent(id,
                key -> entityService.getUserById(id)
        );
    }
}
