package com.example.defecttrackerserver.core.user.user;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserSpecification {

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
