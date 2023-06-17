package com.example.defecttrackerserver.core.action;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService{

    private final ActionRepository actionRepository;
    private final ModelMapper modelMapper;
    private final ActionMapper actionMapper;

    @Override
    public ActionDto saveAction(ActionDto actionDto) {
        Action action = new Action();
        actionDto.setId(null);
        actionMapper.checkNullOrEmptyFields(actionDto);
        Action newAction = actionMapper.map(actionDto, action);
        newAction.setIsCompleted(false);
        newAction.setCreatedOn(LocalDateTime.now());
        Action savedAction = actionRepository.save(newAction);
        return modelMapper.map(savedAction, ActionDto.class);
    }

    @Override
    public ActionDto getActionById(Integer id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));
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
    @Transactional
    public ActionDto updateAction(ActionDto actionDto) {
        Action actionToUpdate = actionRepository.findById(actionDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionDto.getId()));
        actionMapper.checkNullOrEmptyFields(actionDto);

        actionToUpdate.setIsCompleted(actionDto.getIsCompleted());
        Action mappedAction = actionMapper.map(actionDto, actionToUpdate);

        Action updatedAction = actionRepository.save(mappedAction);
        return modelMapper.map(updatedAction, ActionDto.class);
    }

    @Override
    public void deleteAction(Integer id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action not found"));
        actionRepository.delete(action);
    }
}
