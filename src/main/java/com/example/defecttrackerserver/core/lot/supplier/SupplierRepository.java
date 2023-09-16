package com.example.defecttrackerserver.core.lot.supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Supplier}.
 */
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Optional<Supplier> findByName(String name);

    @Query("SELECT s FROM Supplier s WHERE s.id IN :supplierIds")
    List<Supplier> findByIds(@Param("supplierIds") List<Integer> supplierIds);
}
