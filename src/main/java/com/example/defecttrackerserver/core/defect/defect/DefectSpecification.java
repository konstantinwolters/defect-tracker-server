package com.example.defecttrackerserver.core.defect.defect;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefectSpecification {

    public Specification<Defect> createSpecification(
            String searchTerm,
            List<Integer> lotIds,
            List<Integer> materials,
            List<Integer> suppliers,
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

        if (materials != null && !materials.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("lot").get("material").get("id").in(materials));
        }

        if (suppliers != null && !suppliers.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("lot").get("suppliers").get("id").in(suppliers));
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
