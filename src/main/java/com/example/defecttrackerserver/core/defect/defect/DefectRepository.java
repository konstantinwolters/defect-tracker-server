package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.user.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DefectRepository extends JpaRepository<Defect, Integer>, JpaSpecificationExecutor<Defect> {
    @Query("SELECT DISTINCT d.createdAt FROM Defect d WHERE d.id IN :defectIds")
    Set<LocalDateTime> findDistinctCreatedAt(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.changedAt FROM Defect d WHERE d.id IN :defectIds")
    Set<LocalDateTime> findDistinctChangedAt(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.defectStatus FROM Defect d WHERE d.id IN :defectIds")
    Set<DefectStatus> findDistinctDefectStatuses(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.causationCategory FROM Defect d WHERE d.id IN :defectIds")
    Set<CausationCategory> findDistinctCausationCategories(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.location FROM Defect d WHERE d.id IN :defectIds")
    Set<Location> findDistinctLocations(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.lot FROM Defect d WHERE d.id IN :defectIds")
    Set<Lot> findDistinctLots(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.lot.material FROM Defect d WHERE d.id IN :defectIds")
    Set<Material> findDistinctMaterials(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.lot.supplier FROM Defect d WHERE d.id IN :defectIds")
    Set<Supplier> findDistinctSuppliers(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.process FROM Defect d WHERE d.id IN :defectIds")
    Set<Process> findDistinctProcesses(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.defectType FROM Defect d WHERE d.id IN :defectIds")
    Set<DefectType> findDistinctDefectTypes(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.createdBy FROM Defect d WHERE d.id IN :defectIds")
    Set<User> findDistinctCreatedBy(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.changedBy FROM Defect d WHERE d.id IN :defectIds")
    Set<User> findDistinctChangedBy(@Param("defectIds") List<Integer> defectIds);

    Set<Defect> findByDefectStatusId(Integer id);

    Set<Defect> findByDefectTypeId(Integer id);

    Set<Defect> findByProcessId(Integer id);

    Set<Defect> findByLocationId(Integer id);

    Set<Defect> findByCausationCategoryId(Integer id);

    Set<Defect> findByCreatedById(Integer id);
    Set<Defect> findByChangedById(Integer id);
}
