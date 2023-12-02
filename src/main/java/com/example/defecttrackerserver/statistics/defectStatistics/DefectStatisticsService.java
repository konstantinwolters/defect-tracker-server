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

        List<CausationCategoryCount> causationCategoryCounts = causationCategories.stream().map(category -> {
            Long count = defectStatisticsRepository.countByCausationCategory(category);
            CausationCategoryCount categoryCount = new CausationCategoryCount();
            categoryCount.setName(category.getName());
            categoryCount.setCount(count);
            return categoryCount;
        }).toList();

        List<DefectStatusCount> defectStatusCounts = defectStatuses.stream().map(defectStatus -> {
            Long count = defectStatisticsRepository.countByDefectStatus(defectStatus);
            DefectStatusCount defectStatusCount = new DefectStatusCount();
            defectStatusCount.setName(defectStatus.getName());
            defectStatusCount.setCount(count);
            return defectStatusCount;
        }).toList();

        List<LotCount> lotCounts = lots.stream().map(lot -> {
            Long count = defectStatisticsRepository.countByLot(lot);
            LotCount lotCount = new LotCount();
            lotCount.setName(lot.getLotNumber());
            lotCount.setCount(count);
            return lotCount;
        }).toList();

        List<MaterialCount> materialCounts = materials.stream().map(material -> {
            Long count = defectStatisticsRepository.countByMaterial(material);
            MaterialCount materialCount = new MaterialCount();
            materialCount.setName(material.getName());
            materialCount.setCount(count);
            return materialCount;
        }).toList();

        List<LocationCount> locationCounts = locations.stream().map(location -> {
            Long count = defectStatisticsRepository.countByLocation(location);
            LocationCount locationCount = new LocationCount();
            locationCount.setName(location.getName());
            locationCount.setCount(count);
            return locationCount;
        }).toList();

        List<SupplierCount> supplierCounts = suppliers.stream().map(supplier -> {
            Long count = defectStatisticsRepository.countBySupplier(supplier);
            SupplierCount supplierCount = new SupplierCount();
            supplierCount.setName(supplier.getName());
            supplierCount.setCount(count);
            return supplierCount;
        }).toList();

        List<ProcessCount> processCounts = processes.stream().map(process -> {
            Long count = defectStatisticsRepository.countByProcess(process);
            ProcessCount processCount = new ProcessCount();
            processCount.setName(process.getName());
            processCount.setCount(count);
            return processCount;
        }).toList();

        List<YearAndMonthCounts> monthAndYearCounts = yearMonthPairs.stream().map(pair -> {
            Long count = defectStatisticsRepository.countByYearAndMonth(pair.getYear(), pair.getMonth());
            YearAndMonthCounts monthAndYearCount = new YearAndMonthCounts();
            monthAndYearCount.setYear(pair.getYear());
            monthAndYearCount.setMonth(pair.getMonth());
            monthAndYearCount.setCount(count);
            return monthAndYearCount;
        }).toList();

        List<YearCount> yearCounts = years.stream().map(year -> {
            Long count = defectStatisticsRepository.countByYear(year);
            YearCount yearCount = new YearCount();
            yearCount.setYear(year);
            yearCount.setCount(count);
            return yearCount;
        }).toList();

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
}
