package com.example.defecttrackerserver.core.lot.lot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LotRepository extends JpaRepository<Lot, Integer> {
    Optional<Lot> findByLotNumber(String lotNumber);
}
