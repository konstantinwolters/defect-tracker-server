package com.example.defecttrackerserver.core.action;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {
    List<Action> findByCreatedBy_Id(Integer userId);
}
