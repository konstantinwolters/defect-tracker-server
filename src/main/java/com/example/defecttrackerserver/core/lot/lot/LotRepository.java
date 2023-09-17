package com.example.defecttrackerserver.core.lot.lot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Lot}.
 */
public interface LotRepository extends JpaRepository<Lot, Integer> {
    Optional<Lot> findByLotNumber(String lotNumber);

    @Query("SELECT l FROM Lot l WHERE l.lotNumber IN :lotNumbers")
    List<Lot> findByLotNumbers(@Param("lotNumbers") List<String> lotNumbers);
}
