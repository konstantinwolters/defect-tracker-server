package com.example.defecttrackerserver.core.action;

import java.util.List;

public interface ActionService {

    ActionDto saveAction(ActionDto action);
    ActionDto getActionById(Integer id);
    List<ActionDto> getAllActions();
    List<ActionDto> getAllActionsByUserCreatedId(Integer userId);

    List<ActionDto> getFilteredActions(
            String dueDateStart,
            String dueDateEnd,
            Boolean isCompleted,
            List<Integer> assignedUserIds,
            List<Integer> defectIds,
            String createdOnStart,
            String createdOnEnd,
            List<Integer> createdByIds);

    void closeAction(Integer actionId, Boolean isCompleted);
    ActionDto updateAction(ActionDto action);
    void deleteAction(Integer id);
}
