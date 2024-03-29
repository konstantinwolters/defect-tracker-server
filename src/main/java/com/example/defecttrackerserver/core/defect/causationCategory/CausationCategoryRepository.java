package com.example.defecttrackerserver.core.defect.causationCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface managing {@link CausationCategory}.
 */
public interface CausationCategoryRepository extends JpaRepository<CausationCategory, Integer> {
    Optional<CausationCategory> findByName(String name);
}
