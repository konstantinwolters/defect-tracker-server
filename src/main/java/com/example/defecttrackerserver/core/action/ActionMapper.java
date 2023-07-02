package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ActionMapper {
    private final UserRepository userRepository;
    private final DefectRepository defectRepository;
    private final UserMapper userMapper;

    public Action map(ActionDto actionDto, Action action){
        checkNullOrEmptyFields(actionDto);

        action.setDescription(actionDto.getDescription());
        action.setDueDate(actionDto.getDueDate());
        action.setIsCompleted(actionDto.getIsCompleted());
        action.setCreatedOn(actionDto.getCreatedOn());

        Defect defect = defectRepository.findById(actionDto.getDefect())
                .orElseThrow(()-> new EntityNotFoundException("Defect not found with id: "
                        + actionDto.getDefect()));
        defect.addAction(action);

        action.setCreatedBy(userRepository.findById(actionDto.getCreatedBy().getId())
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: "
                        + actionDto.getCreatedBy().getId())));

        Set<User> assignedUsers = actionDto.getAssignedUsers().stream()
                .map(userDto -> userRepository.findById(userDto.getId())
                        .orElseThrow(()-> new EntityNotFoundException("User not found with id: " + userDto.getId())))
                .collect(Collectors.toSet());

        action.setAssignedUsers(assignedUsers);
        assignedUsers.forEach(user -> user.addAssignedAction(action));

        return action;
    }

    public ActionDto mapToDto(Action action){
        ActionDto actionDto = new ActionDto();
        actionDto.setId(action.getId());
        actionDto.setDescription(action.getDescription());
        actionDto.setIsCompleted(action.getIsCompleted());
        actionDto.setDueDate(action.getDueDate());
        actionDto.setAssignedUsers(
                action.getAssignedUsers().stream()
                        .map(userMapper::mapToDto)
                        .collect(Collectors.toSet()));
        actionDto.setDefect(action.getDefect().getId());
        actionDto.setCreatedOn(action.getCreatedOn());
        actionDto.setCreatedBy(userMapper.mapToDto(action.getCreatedBy()));
        return actionDto;
    }

    public void checkNullOrEmptyFields(ActionDto actionDto) {
        if(actionDto.getDueDate() == null)
            throw new IllegalArgumentException("Due date must not be null");
        if(actionDto.getCreatedOn() == null)
            throw new IllegalArgumentException("CreatedOn must not be null");
        if(actionDto.getDescription() == null || actionDto.getDescription().isEmpty())
            throw new IllegalArgumentException("Description must not be null or empty");
        if(actionDto.getAssignedUsers() == null || actionDto.getAssignedUsers().isEmpty())
            throw new IllegalArgumentException("AssignedUsers must not be null or empty");
        if(actionDto.getDefect() == null)
            throw new IllegalArgumentException("Defect must not be null");
        if(actionDto.getCreatedBy() == null)
            throw new IllegalArgumentException("CreatedBy must not be null");
    }
}
