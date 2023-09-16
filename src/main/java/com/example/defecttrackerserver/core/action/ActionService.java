package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.response.PaginatedResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing {@link Action} entities.
 */
public interface ActionService {
    ActionDto saveAction(ActionDto action);
    ActionDto getActionById(Integer id);
    PaginatedResponse<ActionDto> getActions(
            String searchTerm,
            LocalDate dueDateStart,
            LocalDate dueDateEnd,
            Boolean isCompleted,
            String assignedUserIds,
            String defectIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedEnd,
            String createdByIds,
            String changedByIds,
            Integer page,
            Integer size,
            String sort);
    ActionFilterValues getActionFilterValues(List<Action> actions);
    void closeAction(Integer actionId);
    ActionDto updateAction(Integer actionId, ActionDto action);
    void deleteAction(Integer id);
}
