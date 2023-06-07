package com.example.defecttrackerserver.core.action;

import java.util.List;

public interface ActionService {

    ActionDto saveAction(ActionDto action);
    ActionDto getActionById(Integer id);
    List<ActionDto> getAllActions();
    List<ActionDto> getAllActionsByDefectId(Integer defectId);
    List<ActionDto> getAllActionsByUserCreatedId(Integer userId);
    List<ActionDto> getAllActionsByUserAssignedId(Integer userId);
    List<ActionDto> getAllActionsByIsCompleted(Boolean isCompleted);
    List<ActionDto> getAllActionsByDate(String date);
    List<ActionDto> getAllActionsByTimeFrame(String date);
    ActionDto updateAction(ActionDto action);
    void deleteAction(Integer id);
}
