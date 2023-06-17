package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.user.user.User;
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

    public Action map(ActionDto actionDto, Action action){
        action.setDescription(actionDto.getDescription());
        action.setDueDate(actionDto.getDueDate());

        action.setDefect(defectRepository.findById(actionDto.getDefect())
                .orElseThrow(()-> new EntityNotFoundException("Defect not found with id: "
                        + actionDto.getDefect())));

        action.setCreatedBy(userRepository.findById(actionDto.getCreatedBy().getId())
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: "
                        + action.getCreatedBy().getId())));

        Set<User> assignedUsers = actionDto.getAssignedUsers().stream()
                .map(userDto -> userRepository.findById(userDto.getId())
                        .orElseThrow(()-> new EntityNotFoundException("User not found with id: " + userDto.getId())))
                .collect(Collectors.toSet());

        action.setAssignedUsers(assignedUsers);
        assignedUsers.forEach(user -> user.addAssignedAction(action));

        return action;
    }

    public void checkNullOrEmptyFields(ActionDto actionDto) {
        if(actionDto.getDueDate() == null)
            throw new IllegalArgumentException("Username must not be null");
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
