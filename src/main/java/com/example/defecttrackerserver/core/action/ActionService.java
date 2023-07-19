package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActionService {
    ActionDto saveAction(ActionDto action);
    ActionDto getActionById(Integer id);
    PaginatedResponse<ActionDto> getActions(
            String dueDateStart,
            String dueDateEnd,
            Boolean isCompleted,
            List<Integer> assignedUserIds,
            List<Integer> defectIds,
            String createdOnStart,
            String createdOnEnd,
            List<Integer> createdByIds,
            Pageable pageable);
    ActionFilterValues getActionFilterValues(List<Action> actions);
    void closeAction(Integer actionId, Boolean isCompleted);
    ActionDto updateAction(Integer actionId, ActionDto action);
    void deleteAction(Integer id);
}
