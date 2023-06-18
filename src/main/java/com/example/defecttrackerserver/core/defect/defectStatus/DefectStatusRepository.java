package com.example.defecttrackerserver.core.defect.defectStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefectStatusRepository extends JpaRepository<DefectStatus, Integer> {
    Optional<DefectStatus> findByName(String name);
}
