package com.example.defecttrackerserver.statistics.defectStatistics;

import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.lot.supplier.SupplierRepository;
import com.example.defecttrackerserver.statistics.CausationCategoryCount;
import com.example.defecttrackerserver.statistics.MonthAndYearCount;
import com.example.defecttrackerserver.statistics.YearCount;
import com.example.defecttrackerserver.statistics.YearMonthPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefectStatisticsService {
    private final DefectStatisticsRepository defectStatisticsRepository;
    private final CausationCategoryRepository causationCategoryRepository;
    private final DefectStatusRepository defectStatusRepository;
    private final LotRepository lotRepository;
    private final MaterialRepository materialRepository;
    private final LocationRepository locationRepository;
    private final SupplierRepository supplierRepository;
    private final ProcessRepository processRepository;

    private List<MonthAndYearCount> monthAndYearCounts;
    private List<YearCount> yearCounts;


    public DefectStatisticsDto getDefectStatistics(){
        List<CausationCategory> causationCategories = causationCategoryRepository.findAll();
        List<DefectStatus> defectStatuses = defectStatusRepository.findAll();
        List<Lot> lots = lotRepository.findAll();
        List<Material> materials = materialRepository.findAll();
        List<Location> locations = locationRepository.findAll();
        List<Supplier> suppliers = supplierRepository.findAll();
        List<Process> processes = processRepository.findAll();
        List<YearMonthPair> yearMonthPairs = defectStatisticsRepository.findDistinctYearAndMonth();
        List<Integer> years = defectStatisticsRepository.findDistinctYears();

        List<CausationCategoryCount> causationCategoryCounts = causationCategories.stream().map(category -> {
            Long count = defectStatisticsRepository.countByCausationCategory(category);
            CausationCategoryCount categoryCount = new CausationCategoryCount();
            categoryCount.setName(category.getName());
            categoryCount.setCount(count.intValue());
            return categoryCount;
        }).toList();

        DefectStatisticsDto defectStatistics = new DefectStatisticsDto();
        defectStatistics.setCausationCategoryCounts(causationCategoryCounts);

        return defectStatistics;
    }
}
