package com.example.defecttrackerserver.statistics.actionStatistics;

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

public interface ActionStatisticsRepository extends JpaRepository<Defect, Integer>, JpaSpecificationExecutor<Defect> {

    Long countByCausationCategory(CausationCategory causationCategory);

    @Query("SELECT COUNT(d) FROM Defect d WHERE YEAR(d.createdAt) = :year AND MONTH(d.createdAt) = :month")
    Long countByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(d) FROM Defect d WHERE YEAR(d.createdAt) = :year")
    Long countByYear(@Param("year") int year);

    @Query("SELECT DISTINCT YEAR(d.createdAt) FROM Defect d ORDER BY YEAR(d.createdAt)")
    List<Integer> findDistinctYears();

    @Query("SELECT DISTINCT new com.example.defecttrackerserver.statistics.YearMonthPair(YEAR(d.createdAt), " +
            "MONTH(d.createdAt)) FROM Defect d ORDER BY YEAR(d.createdAt), MONTH(d.createdAt)")
    List<YearMonthPair> findDistinctYearAndMonth();
}
