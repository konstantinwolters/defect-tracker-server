package com.example.defecttrackerserver.core.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Location}.
 */
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByName(String locationName);
}
