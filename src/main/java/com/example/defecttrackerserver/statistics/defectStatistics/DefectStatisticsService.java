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
import com.example.defecttrackerserver.statistics.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        List<CausationCategoryCount> causationCategoryCounts = calculateCausationCategoryCounts(causationCategories);
        List<DefectStatusCount> defectStatusCounts = calculateDefectStatusCounts(defectStatuses);
        List<LotCount> lotCounts = calculateLotCounts(lots);
        List<MaterialCount> materialCounts = calculateMaterialCounts(materials);
        List<LocationCount> locationCounts = calculateLocationCounts(locations);
        List<SupplierCount> supplierCounts = calculateSupplierCounts(suppliers);
        List<ProcessCount> processCounts = calculateProcessCounts(processes);
        List<YearAndMonthCounts> monthAndYearCounts = calculateMonthAndYearCounts(yearMonthPairs);
        List<YearCount> yearCounts = calculateYearCounts(years);

        DefectStatisticsDto defectStatistics = new DefectStatisticsDto();
        defectStatistics.setCausationCategoryCounts(causationCategoryCounts);
        defectStatistics.setDefectStatusCounts(defectStatusCounts);
        defectStatistics.setLotCounts(lotCounts);
        defectStatistics.setMaterialCounts(materialCounts);
        defectStatistics.setLocationCounts(locationCounts);
        defectStatistics.setSupplierCounts(supplierCounts);
        defectStatistics.setProcessCounts(processCounts);
        defectStatistics.setMonthAndYearCounts(monthAndYearCounts);
        defectStatistics.setYearCounts(yearCounts);

        return defectStatistics;
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

    private List<DefectStatusCount> calculateDefectStatusCounts(List<DefectStatus> defectStatuses){
        return defectStatuses.stream().map(defectStatus -> {
            Long count = defectStatisticsRepository.countByDefectStatus(defectStatus);
            DefectStatusCount defectStatusCount = new DefectStatusCount();
            defectStatusCount.setName(defectStatus.getName());
            defectStatusCount.setCount(count);
            return defectStatusCount;
        }).toList();
    }

    private List<LotCount> calculateLotCounts(List<Lot> lots){
        return lots.stream().map(lot -> {
            Long count = defectStatisticsRepository.countByLot(lot);
            LotCount lotCount = new LotCount();
            lotCount.setName(lot.getLotNumber());
            lotCount.setCount(count);
            return lotCount;
        }).toList();
    }

    private List<MaterialCount> calculateMaterialCounts(List<Material> materials){
        return materials.stream().map(material -> {
            Long count = defectStatisticsRepository.countByMaterial(material);
            MaterialCount materialCount = new MaterialCount();
            materialCount.setName(material.getName());
            materialCount.setCount(count);
            return materialCount;
        }).toList();
    }

    private List<LocationCount> calculateLocationCounts(List<Location> locations){
        return locations.stream().map(location -> {
            Long count = defectStatisticsRepository.countByLocation(location);
            LocationCount locationCount = new LocationCount();
            locationCount.setName(location.getName());
            locationCount.setCount(count);
            return locationCount;
        }).toList();
    }

    private List<SupplierCount> calculateSupplierCounts(List<Supplier> suppliers){
        return suppliers.stream().map(supplier -> {
            Long count = defectStatisticsRepository.countBySupplier(supplier);
            SupplierCount supplierCount = new SupplierCount();
            supplierCount.setName(supplier.getName());
            supplierCount.setCount(count);
            return supplierCount;
        }).toList();
    }

    private List<ProcessCount> calculateProcessCounts(List<Process> processes){
        return processes.stream().map(process -> {
            Long count = defectStatisticsRepository.countByProcess(process);
            ProcessCount processCount = new ProcessCount();
            processCount.setName(process.getName());
            processCount.setCount(count);
            return processCount;
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
