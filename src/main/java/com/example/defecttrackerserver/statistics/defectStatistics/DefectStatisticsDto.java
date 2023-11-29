package com.example.defecttrackerserver.statistics.defectStatistics;

import com.example.defecttrackerwebclient.statistics.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DefectStatisticsDto {
    private List<CausationCategoryCount> causationCategoryCounts;
    private List<DefectStatusCount> defectStatusCounts;
    private List<DefectTypeCount> defectTypeCounts;
    private List<LotCount> lotCounts;
    private List<MaterialCount> materialCounts;
    private List<LocationCount> locationCounts;
    private List<SupplierCount> supplierCounts;
    private List<MonthCount> monthCounts;
    private List<YearCount> yearCounts;
    private List<ProcessCount> processCounts;
}
