package com.example.defecttrackerserver.core.action;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer>, JpaSpecificationExecutor<Action> {
    List<Action> findByCreatedBy_Id(Integer userId);
}
