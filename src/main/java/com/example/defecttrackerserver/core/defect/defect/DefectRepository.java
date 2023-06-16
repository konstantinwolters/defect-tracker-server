package com.example.defecttrackerserver.core.defect.defect;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DefectRepository extends JpaRepository<Defect, Integer> {
}
