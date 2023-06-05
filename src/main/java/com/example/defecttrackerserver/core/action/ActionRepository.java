package com.example.defecttrackerserver.core.action;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {
    List<Action> findByDefect_Id(Integer defectId);
    List<Action> findByCreatedBy_Id(Integer userId);
    List<Action> findByAssigned_Id(Integer userId);
    List<Action> findByIsCompleted(Boolean status);
    List<Action> findByCreatedOn(LocalDateTime date);
    List<Action> findByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
}
