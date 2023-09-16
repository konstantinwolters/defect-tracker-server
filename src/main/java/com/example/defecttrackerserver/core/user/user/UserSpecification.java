package com.example.defecttrackerserver.core.user.user;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for creating JPA Specifications for filtering Action entities.
 */
@Service
public class UserSpecification {

    /**
     * Creates a JPA Specification based on various filter criteria.
     *
     * @param searchTerm Search term for description or ID.
     * @param isActive Activity status of a user.
     * @param locationIdList List of location IDs where user is located.
     * @param roleIdList List of role IDs of roles assigned to a user.
     * @param createdAtStart Start date for creation timestamp range.
     * @param createdAtEnd End date for creation timestamp range.
     * @param changedAtStart Start date for last modification timestamp range.
     * @param changedAtEnd End date for last modification timestamp range.
     * @param createdByIdList List of user IDs who created the action.
     * @param changedByIdList List of user IDs who last modified the action.
     * @return A JPA Specification for filtering Action entities.
     */
    public Specification<User> createSpecification(
            String searchTerm,
            Boolean isActive,
            List<Integer> locationIdList,
            List<Integer> roleIdList,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            List<Integer> createdByIdList,
            List<Integer> changedByIdList
    ){
        Specification<User> spec = Specification.where(null);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("firstName")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("lastName")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("mail")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("username")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("id").as(String.class)), "%" + searchTerm.toLowerCase() + "%"),
                            cb.and(
                                    cb.isNotNull(root.get("customId")),
                                    cb.like(cb.lower(root.get("customId")), "%" + searchTerm.toLowerCase() + "%")
                            )
                    )
            );
        }

        if(isActive != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), isActive));
        }

        if(locationIdList != null && !locationIdList.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("location").get("id").in(locationIdList));
        }

        if(roleIdList != null && !roleIdList.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("defect").get("id").in(roleIdList));
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
            spec = spec.and((root, query, cb) -> root.get("createdById").in(createdByIdList));
        }

        if(changedByIdList != null && !changedByIdList.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("changedById").in(changedByIdList));
        }

        return spec;
    }
}
