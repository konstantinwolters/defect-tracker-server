package com.example.defecttrackerserver.core.defect.defect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Arrays;
import java.util.List;

public interface DefectRepository extends JpaRepository<Defect, Integer>, JpaSpecificationExecutor<Defect> {
    List<Defect> findAllByIdIn(List<Integer> ids);
}
