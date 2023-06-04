package com.example.defecttrackerserver.core.action;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Action getAction(Integer id) {
        return null;
    }

    @Override
    public List<Action> getAllActions() {
        return null;
    }

    @Override
    public List<ActionDto> getActionsByDefectId(Integer defectId) {
        return null;
    }

    @Override
    public List<ActionDto> getActionsByUserCreatedId(Integer userId) {
        return null;
    }

    @Override
    public List<ActionDto> getActionsByUserAssignedId(Integer userId) {
        return null;
    }

    @Override
    public List<ActionDto> getActionsByIsCompleted(Boolean isCompleted) {
        return null;
    }

    @Override
    public List<ActionDto> getActionsByDate(String date) {
        return null;
    }

    @Override
    public List<ActionDto> getActionsByTimeFrame(String date) {
        return null;
    }

    @Override
    public ActionDto updateAction(ActionDto actionDto) {
        return null;
    }

    @Override
    public void deleteAction(Integer id) {

    }
}
