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
import com.example.defecttrackerserver.statistics.YearMonthPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefectStatisticsServiceTest {

    @Mock
    private DefectStatisticsRepository defectStatisticsRepository;

    @Mock
    private CausationCategoryRepository causationCategoryRepository;

    @Mock
    private DefectStatusRepository defectStatusRepository;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ProcessRepository processRepository;

    @InjectMocks
    private DefectStatisticsService defectStatisticsService;

    @Test
    void shouldReturnDefectStatistics() {
        List<CausationCategory> causationCategories = new ArrayList<>(List.of(new CausationCategory()));
        List<DefectStatus> defectStatuses = new ArrayList<>(List.of(new DefectStatus()));
        List<Lot> lots = new ArrayList<>(List.of(new Lot()));
        List<Material> materials = new ArrayList<>(List.of(new Material()));
        List<Location> locations = new ArrayList<>(List.of(new Location()));
        List<Supplier> suppliers = new ArrayList<>(List.of(new Supplier()));
        List<Process> processes = new ArrayList<>(List.of(new Process()));
        List<YearMonthPair> yearMonthPairs = new ArrayList<>(List.of(new YearMonthPair()));
        List<Integer> years = new ArrayList<>(List.of(2023));

        when(causationCategoryRepository.findAll()).thenReturn(causationCategories);
        when(defectStatusRepository.findAll()).thenReturn(defectStatuses);
        when(lotRepository.findAll()).thenReturn(lots);
        when(materialRepository.findAll()).thenReturn(materials);
        when(locationRepository.findAll()).thenReturn(locations);
        when(supplierRepository.findAll()).thenReturn(suppliers);
        when(processRepository.findAll()).thenReturn(processes);
        when(defectStatisticsRepository.findDistinctYearAndMonth()).thenReturn(yearMonthPairs);
        when(defectStatisticsRepository.findDistinctYears()).thenReturn(years);

        when(defectStatisticsRepository.countByCausationCategory(any(CausationCategory.class))).thenReturn(1L);
        when(defectStatisticsRepository.countByDefectStatus(any(DefectStatus.class))).thenReturn(1L);
        when(defectStatisticsRepository.countByLot(any(Lot.class))).thenReturn(1L);
        when(defectStatisticsRepository.countByMaterial(any(Material.class))).thenReturn(1L);
        when(defectStatisticsRepository.countByLocation(any(Location.class))).thenReturn(1L);
        when(defectStatisticsRepository.countBySupplier(any(Supplier.class))).thenReturn(1L);
        when(defectStatisticsRepository.countByProcess(any(Process.class))).thenReturn(1L);
        when(defectStatisticsRepository.countByYearAndMonth(anyInt(), anyInt())).thenReturn(1L);
        when(defectStatisticsRepository.countByYear(any(Integer.class))).thenReturn(1L);

        DefectStatisticsDto defectStatisticsDto = defectStatisticsService.getDefectStatistics();

        assertEquals(defectStatisticsDto.getDefectStatusCounts().size(), 1);
        assertEquals(defectStatisticsDto.getCausationCategoryCounts().size(), 1);
        assertEquals(defectStatisticsDto.getLotCounts().size(), 1);
        assertEquals(defectStatisticsDto.getMaterialCounts().size(), 1);
        assertEquals(defectStatisticsDto.getLocationCounts().size(), 1);
        assertEquals(defectStatisticsDto.getSupplierCounts().size(), 1);
        assertEquals(defectStatisticsDto.getProcessCounts().size(), 1);
        assertEquals(defectStatisticsDto.getMonthAndYearCounts().size(), 1);
        assertEquals(defectStatisticsDto.getYearCounts().size(), 1);
    }
}
