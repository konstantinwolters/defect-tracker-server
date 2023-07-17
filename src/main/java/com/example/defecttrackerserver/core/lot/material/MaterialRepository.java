package com.example.defecttrackerserver.core.lot.material;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Optional<Material> findByName(String name);
}
