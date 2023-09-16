package com.example.defecttrackerserver.core.defect.defect;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for creating JPA Specifications for filtering Defect entities.
 */
@Service
public class DefectSpecification {

    /**
     * Creates a JPA Specification based on various filter criteria.
     *
     * @param lotIds List of IDs of selected lots.
     * @param materialIds List of IDs of selected materials.
     * @param supplierIds List of IDs of selected suppliers.
     * @param defectStatusIds List of IDs of selected defect statuses.
     * @param causationCategoriesIds List of IDs of selected causation categories.
     * @param searchTerm Search term for description or ID.
     * @param createdAtStart Start date for creation timestamp range.
     * @param createdAtEnd End date for creation timestamp range.
     * @param changedAtStart Start date for last modification timestamp range.
     * @param changedAtEnd End date for last modification timestamp range.
     * @param locationIds List of IDs of selected locations.
     * @param processIds List of IDs of selected processes.
     * @param defectTypeIds List of IDs of selected defect types.
     * @param createdByIds List of user IDs who created the action.
     * @param changedByIds List of user IDs who last modified the action.
     * @return A JPA Specification for filtering Action entities.
     */
    public Specification<Defect> createSpecification(
            String searchTerm,
            List<Integer> lotIds,
            List<Integer> materialIds,
            List<Integer> supplierIds,
            List<Integer> defectStatusIds,
            List<Integer> causationCategoriesIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            List<Integer> locationIds,
            List<Integer> processIds,
            List<Integer> defectTypeIds,
            List<Integer> createdByIds,
            List<Integer> changedByIds
    ) {
        Specification<Defect> spec = Specification.where(null);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("description")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("id").as(String.class)), "%" + searchTerm.toLowerCase() + "%")
                    )
            );
        }

        if (lotIds != null && !lotIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("lot").get("id").in(lotIds));
        }

        if (materialIds != null && !materialIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("lot").get("material").get("id").in(materialIds));
        }

        if (supplierIds != null && !supplierIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("lot").get("suppliers").get("id").in(supplierIds));
        }

        if (defectStatusIds != null && !defectStatusIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("defectStatus").get("id").in(defectStatusIds));
        }

        if (causationCategoriesIds != null && !causationCategoriesIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("causationCategory").get("id").in(causationCategoriesIds));
        }

        if (createdAtStart != null && createdAtEnd != null) {
            LocalDateTime startOfDay = createdAtStart.atStartOfDay();
            LocalDateTime endOfDay = createdAtEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), startOfDay, endOfDay));
        }

        if (changedAtStart != null && changedAtEnd != null) {
            LocalDateTime startOfDay = changedAtStart.atStartOfDay();
            LocalDateTime endOfDay = changedAtEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("changedAt"), startOfDay, endOfDay));
        }

        if (locationIds != null && !locationIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("location").get("id").in(locationIds));
        }

        if (processIds != null && !processIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("process").get("id").in(processIds));
        }

        if (defectTypeIds != null && !defectTypeIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("defectType").get("id").in(defectTypeIds));
        }

        if (createdByIds != null && !createdByIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("createdBy").get("id").in(createdByIds));
        }

        if (changedByIds != null && !changedByIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("changedBy").get("id").in(changedByIds));
        }

        return  spec;
    }
}
