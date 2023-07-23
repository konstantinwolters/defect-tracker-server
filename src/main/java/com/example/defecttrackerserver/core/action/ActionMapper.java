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
        action.setDescription(actionDto.getDescription());
        action.setDueDate(actionDto.getDueDate());
        action.setIsCompleted(actionDto.getIsCompleted());
        action.setCreatedAt(actionDto.getCreatedAt());

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
        actionDto.setCreatedAt(action.getCreatedAt());
        actionDto.setCreatedBy(userMapper.mapToDto(action.getCreatedBy()));
        return actionDto;
    }
}
