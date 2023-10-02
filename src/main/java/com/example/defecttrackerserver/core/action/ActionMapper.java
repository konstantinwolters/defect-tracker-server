package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserRepository userRepository;
    private final DefectRepository defectRepository;
    private final UserMapper userMapper;

    private Map<Integer, User> userCache = new HashMap<>();

    public Action map(ActionDto actionDto, Action action){
        Defect defect = getDefectById(actionDto.getDefect());
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

        //Maintain relationships
        defect.addAction(action);
        assignedUsers.forEach(user -> user.addAssignedAction(action));

        return action;
    }

    public ActionDto mapToDto(Action action){
        long start = System.currentTimeMillis();

        Set<UserDto> assignedUsers = action.getAssignedUsers().stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toSet());

        UserDto changedBy = action.getChangedBy() != null ? userMapper.mapToDto(action.getChangedBy()) : null;

        ActionDto actionDto = new ActionDto();
        actionDto.setId(action.getId());
        actionDto.setDescription(action.getDescription());
        actionDto.setIsCompleted(action.getIsCompleted());
        actionDto.setDueDate(action.getDueDate());
        actionDto.setAssignedUsers(assignedUsers);
        actionDto.setDefect(action.getDefect().getId());
        actionDto.setCreatedAt(action.getCreatedAt());
        actionDto.setChangedAt(action.getChangedAt());
        actionDto.setCreatedBy(userMapper.mapToDto(action.getCreatedBy()));
        actionDto.setChangedBy(changedBy);

        return actionDto;
    }

    private User getUserById(Integer id){
        return userCache.computeIfAbsent(id,
                key -> userRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("User not found with id: " + id))
        );
    }

    private Defect getDefectById(Integer id){
        return defectRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Defect not found with id: " + id));
    }
}
