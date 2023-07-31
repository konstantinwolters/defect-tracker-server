package com.example.defecttrackerserver.core.defect.defect;

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

    @Query("SELECT DISTINCT d.defectStatus.name FROM Defect d WHERE d.id IN :defectIds")
    Set<String> findDistinctDefectStatusName(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.location.name FROM Defect d WHERE d.id IN :defectIds")
    Set<String> findDistinctLocationName(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.lot.lotNumber FROM Defect d WHERE d.id IN :defectIds")
    Set<String> findDistinctLotNumber(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.process.name FROM Defect d WHERE d.id IN :defectIds")
    Set<String> findDistinctProcessName(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.defectType.name FROM Defect d WHERE d.id IN :defectIds")
    Set<String> findDistinctDefectTypeName(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.createdBy FROM Defect d WHERE d.id IN :defectIds")
    Set<User> findDistinctCreatedBy(@Param("defectIds") List<Integer> defectIds);

    @Query("SELECT DISTINCT d.changedBy FROM Defect d WHERE d.id IN :defectIds")
    Set<User> findDistinctChangedBy(@Param("defectIds") List<Integer> defectIds);
}
