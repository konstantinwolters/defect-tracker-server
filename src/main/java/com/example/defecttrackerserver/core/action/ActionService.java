package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ActionService {
    ActionDto saveAction(ActionDto action);
    ActionDto getActionById(Integer id);
    PaginatedResponse<ActionDto> getActions(
            String searchTerm,
            LocalDate dueDateStart,
            LocalDate dueDateEnd,
            Boolean isCompleted,
            List<Integer> assignedUserIds,
            List<Integer> defectIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedEnd,
            List<Integer> createdByIds,
            List<Integer> changedByIds,
            Pageable pageable);
    ActionFilterValues getActionFilterValues(List<Action> actions);
    void closeAction(Integer actionId);
    ActionDto updateAction(Integer actionId, ActionDto action);
    void deleteAction(Integer id);
}
