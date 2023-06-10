package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.user.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService{

    private final ActionRepository actionRepository;
    private final ModelMapper modelMapper;

    @Override
    public ActionDto saveAction(ActionDto actionDto) {
        Action action = modelMapper.map(actionDto, Action.class);
        action.setIsCompleted(false);
        Action savedAction = actionRepository.save(action);
        return modelMapper.map(savedAction, ActionDto.class);
    }

    @Override
    public ActionDto getActionById(Integer id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action not found"));
        return modelMapper.map(action, ActionDto.class);
    }

    @Override
    public List<ActionDto> getAllActions() {
        List<Action> actions = actionRepository.findAll();
        return actions.stream().map(action -> modelMapper.map(action, ActionDto.class)).toList();
    }

    @Override
    public List<ActionDto> getAllActionsByUserCreatedId(Integer userId) {
        List<Action> actions = actionRepository.findByCreatedBy_Id(userId);
        return actions.stream()
                .map(action -> modelMapper.map(action, ActionDto.class)).toList();
    }

    @Override
    public ActionDto updateAction(ActionDto actionDto) {
        Action actionToUpdate = actionRepository.findById(actionDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionDto.getId()));

        actionToUpdate.setDescription(actionDto.getDescription());

        Set<User> assignedUsers = actionDto.getAssignedUsers().stream()
                .map(userDto -> modelMapper.map(userDto, User.class)).collect(Collectors.toSet());
        actionToUpdate.setAssignedUsers(assignedUsers);

        actionToUpdate.setCreatedBy(modelMapper.map(actionDto.getCreatedBy(), User.class));
        actionToUpdate.setIsCompleted(actionDto.getIsCompleted());

        Action updatedAction = actionRepository.save(actionToUpdate);
        return modelMapper.map(updatedAction, ActionDto.class);
    }

    @Override
    public void deleteAction(Integer id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action not found"));
        actionRepository.delete(action);
    }
}
