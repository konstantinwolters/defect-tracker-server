package com.example.defecttrackerserver.core.action;

import java.util.List;

public interface ActionService {

    ActionDto saveAction(ActionDto action);
    ActionDto getActionById(Integer id);
    List<ActionDto> getAllActions();
    List<ActionDto> getAllActionsByUserCreatedId(Integer userId);
    void closeAction(Integer actionId, Boolean isCompleted);
    ActionDto updateAction(ActionDto action);
    void deleteAction(Integer id);
}
