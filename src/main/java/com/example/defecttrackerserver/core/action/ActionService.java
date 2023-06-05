package com.example.defecttrackerserver.core.action;

import java.util.List;

public interface ActionService {

    ActionDto saveAction(ActionDto action);
    ActionDto getActionById(Integer id);
    List<ActionDto> getAllActions();
    List<ActionDto> getActionsByDefectId(Integer defectId);
    List<ActionDto> getActionsByUserCreatedId(Integer userId);
    List<ActionDto> getActionsByUserAssignedId(Integer userId);
    List<ActionDto> getActionsByIsCompleted(Boolean isCompleted);
    List<ActionDto> getActionsByDate(String date);
    List<ActionDto> getActionsByTimeFrame(String date);
    ActionDto updateAction(ActionDto action);
    void deleteAction(Integer id);
}
