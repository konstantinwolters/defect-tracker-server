package com.example.defecttrackerserver.core.action;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for creating JPA Specifications for filtering Action entities.
 */
@Service
class ActionSpecification {

    /**
     * Creates a JPA Specification based on various filter criteria.
     *
     * @param searchTerm Search term for description or ID.
     * @param dueDateStart Start date for due date range.
     * @param dueDateEnd End date for due date range.
     * @param isCompleted Completion status.
     * @param assignedUserIdList List of user IDs assigned to the action.
     * @param defectIdList List of defect IDs associated with the action.
     * @param createdAtStart Start date for creation timestamp range.
     * @param createdAtEnd End date for creation timestamp range.
     * @param changedAtStart Start date for last modification timestamp range.
     * @param changedAtEnd End date for last modification timestamp range.
     * @param createdByIdList List of user IDs who created the action.
     * @param changedByIdList List of user IDs who last modified the action.
     * @return A JPA Specification for filtering Action entities.
     */
    public Specification<Action> createSpecification(
            String searchTerm,
            LocalDate dueDateStart,
            LocalDate dueDateEnd,
            Boolean isCompleted,
            List<Integer> assignedUserIdList,
            List<Integer> defectIdList,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            List<Integer> createdByIdList,
            List<Integer> changedByIdList
    ){
        Specification<Action> spec = Specification.where(null);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("description")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("id").as(String.class)), "%" + searchTerm.toLowerCase() + "%")
                    )
            );
        }

        if (dueDateStart != null && dueDateEnd != null) {
            LocalDateTime startOfDay = dueDateStart.atStartOfDay();
            LocalDateTime endOfDay = dueDateEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("dueDate"), startOfDay, endOfDay));
        }

        if(isCompleted != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isCompleted"), isCompleted));
        }

        if(assignedUserIdList != null && !assignedUserIdList.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("assignedUsers").get("id").in(assignedUserIdList));
        }

        if(defectIdList != null && !defectIdList.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("defect").get("id").in(defectIdList));
        }

        if (createdAtStart != null && createdAtEnd != null) {
            LocalDateTime startOfDay = createdAtStart.atStartOfDay();
            LocalDateTime endOfDay = createdAtEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), startOfDay, endOfDay));
        }

        if (changedAtStart != null && changedAtEnd != null) {
            LocalDateTime startOfDay = changedAtStart.atStartOfDay();
            LocalDateTime endOfDay = changedAtEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), startOfDay, endOfDay));
        }

        if(createdByIdList != null && !createdByIdList.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("createdBy").get("id").in(createdByIdList));
        }

        if(changedByIdList != null && !changedByIdList.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("changedBy").get("id").in(changedByIdList));
        }

        return spec;
    }
}
