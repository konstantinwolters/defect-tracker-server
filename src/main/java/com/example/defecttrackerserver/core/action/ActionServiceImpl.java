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
    public List<ActionDto> getAllActionsByDefectId(Integer defectId) {
        List<Action> actions = actionRepository.findByDefect_Id(defectId);
        return actions.stream().map(action -> modelMapper.map(action, ActionDto.class)).toList();
    }

    @Override
    public List<ActionDto> getAllActionsByUserCreatedId(Integer userId) {
        return null;
    }

    @Override
    public List<ActionDto> getAllActionsByUserAssignedId(Integer userId) {
        return null;
    }

    @Override
    public List<ActionDto> getAllActionsByIsCompleted(Boolean isCompleted) {
        return null;
    }

    @Override
    public List<ActionDto> getAllActionsByDate(String date) {
        return null;
    }

    @Override
    public List<ActionDto> getAllActionsByTimeFrame(String date) {
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
