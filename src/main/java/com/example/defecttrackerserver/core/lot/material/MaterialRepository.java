package com.example.defecttrackerserver.core.lot.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Optional<Material> findByName(String name);

    @Query("SELECT m FROM Material m WHERE m.id IN :materialIds")
    List<Material> findByIds(@Param("materialIds") List<Integer> materialIds);
}
