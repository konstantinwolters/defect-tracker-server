package com.example.defecttrackerserver.core.defect.defectImage;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing {@link DefectImage}.
 */
public interface DefectImageRepository extends JpaRepository<DefectImage, Integer> {
}
