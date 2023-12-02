package com.example.defecttrackerserver.statistics.defectStatistics;

import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.statistics.YearMonthPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DefectStatisticsRepository extends JpaRepository<Defect, Integer>, JpaSpecificationExecutor<Defect> {

    Long countByCausationCategory(CausationCategory causationCategory);
    Long countByDefectStatus(DefectStatus defectStatus);
    Long countByDefectType(DefectType defectType);
    Long countByLot(Lot lot);
    Long countByLocation(Location location);
    Long countByProcess(Process process);

    @Query("SELECT COUNT(d) FROM Defect d WHERE YEAR(d.createdBy) = :year AND MONTH(d.createdBy) = :month")
    Long countByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(d) FROM Defect d WHERE YEAR(d.createdBy) = :year")
    Long countByYear(@Param("year") int year);

    @Query("SELECT COUNT(d) FROM Defect d JOIN d.lot l WHERE l.material = :material")
    Long countByMaterial(@Param("material") Material material);

    @Query("SELECT COUNT(d) FROM Defect d JOIN d.lot l WHERE l.supplier = :supplier")
    Long countBySupplier(@Param("supplier") Supplier supplier);

    @Query("SELECT DISTINCT YEAR(d.createdBy) FROM Defect d ORDER BY YEAR(d.createdBy)")
    List<Integer> findDistinctYears();

    @Query("SELECT DISTINCT new com.example.defecttrackerserver.statistics.YearMonthPair(YEAR(d.createdBy), " +
            "MONTH(d.createdBy)) FROM Defect d ORDER BY YEAR(d.createdBy), MONTH(d.createdBy)")
    List<YearMonthPair> findDistinctYearAndMonth();
}
