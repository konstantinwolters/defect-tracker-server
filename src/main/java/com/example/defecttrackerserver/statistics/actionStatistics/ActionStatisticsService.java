package com.example.defecttrackerserver.statistics.actionStatistics;

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
import com.example.defecttrackerserver.statistics.*;
import com.example.defecttrackerserver.statistics.defectStatistics.DefectStatisticsDto;
import com.example.defecttrackerserver.statistics.defectStatistics.DefectStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionStatisticsService {
    private final DefectStatisticsRepository defectStatisticsRepository;
    private final CausationCategoryRepository causationCategoryRepository;

    public ActionStatisticsDto getActionStatistics(){
        List<CausationCategory> causationCategories = causationCategoryRepository.findAll();
        List<YearMonthPair> yearMonthPairs = defectStatisticsRepository.findDistinctYearAndMonth();
        List<Integer> years = defectStatisticsRepository.findDistinctYears();

        List<CausationCategoryCount> causationCategoryCounts = calculateCausationCategoryCounts(causationCategories);
        List<YearAndMonthCounts> monthAndYearCounts = calculateMonthAndYearCounts(yearMonthPairs);
        List<YearCount> yearCounts = calculateYearCounts(years);

        ActionStatisticsDto actionStatistics = new ActionStatisticsDto();
        actionStatistics.setCausationCategoryCounts(causationCategoryCounts);
        actionStatistics.setMonthAndYearCounts(monthAndYearCounts);
        actionStatistics.setYearCounts(yearCounts);

        return actionStatistics;
    }

    private List<CausationCategoryCount> calculateCausationCategoryCounts(List<CausationCategory> causationCategories){
        return causationCategories.stream().map(category -> {
            Long count = defectStatisticsRepository.countByCausationCategory(category);
            CausationCategoryCount categoryCount = new CausationCategoryCount();
            categoryCount.setName(category.getName());
            categoryCount.setCount(count);
            return categoryCount;
        }).toList();
    }

    private List<YearAndMonthCounts> calculateMonthAndYearCounts(List<YearMonthPair> yearMonthPairs){
        return yearMonthPairs.stream().map(pair -> {
            Long count = defectStatisticsRepository.countByYearAndMonth(pair.getYear(), pair.getMonth());
            YearAndMonthCounts monthAndYearCount = new YearAndMonthCounts();
            monthAndYearCount.setYear(pair.getYear());
            monthAndYearCount.setMonth(pair.getMonth());
            monthAndYearCount.setCount(count);
            return monthAndYearCount;
        }).toList();
    }

    private List<YearCount> calculateYearCounts(List<Integer> years){
        return years.stream().map(year -> {
            Long count = defectStatisticsRepository.countByYear(year);
            YearCount yearCount = new YearCount();
            yearCount.setYear(year);
            yearCount.setCount(count);
            return yearCount;
        }).toList();
    }
}
