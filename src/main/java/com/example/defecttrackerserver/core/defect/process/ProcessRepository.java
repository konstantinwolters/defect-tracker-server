package com.example.defecttrackerserver.core.defect.process;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessRepository extends JpaRepository<Process, Integer> {
    Optional<Process> findByName(String name);
}
