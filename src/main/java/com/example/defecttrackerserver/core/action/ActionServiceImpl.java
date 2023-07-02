package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService{

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;

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
