package com.example.defecttrackerserver.core.defect.defectType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefectTypeRepository extends JpaRepository<DefectType, Integer> {
    Optional<DefectType> findByName(String name);
}
